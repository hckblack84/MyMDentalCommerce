package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Exception.*;
import com.MyMDentis.MyMDentistComerce.Model.*;
import com.MyMDentis.MyMDentistComerce.Repository.OrderRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PaymentService {

    @Value("${mercadoPago.token}")
    private String token;
    @Value("${frontend.base.url}")
    private String frontendBaseUrl;
    @Value("${backend.base.url}")
    private String backendBaseUrl;
    @Value("${mercadoPago.redirectionPath}")
    private String redirectionPath;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Transactional
    public String createOrderAndGetPaymentUrl(List<DTOReservedPetition> petitions) throws MPException, MPApiException {
        try {
            UserEntity currentUser = findAuthenticatedUser();

            Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setStatus(OrderStatus.PENDING);
            order.setUser(currentUser);

            List<PreferenceItemRequest> preferenceItems = new ArrayList<>();

            for (DTOReservedPetition petition : petitions) {
                Product product = productRepository.findById(petition.getIdProduct())
                        .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE, Entities.PRODUCT, ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE));

                if (product.getStockProduct() < petition.getQuantityReserved()) {
                    throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_REQUEST_CODE, Entities.PRODUCT, "No hay stock para " + product.getProductName());
                }
                if (product.getStockProduct() - petition.getQuantityReserved() < 0) {
                    throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_REQUEST_CODE, Entities.PRODUCT, ExceptionValues.INVALID_STOCK_REQUEST_MESSAGE);
                }
                product.setStockProduct(product.getStockProduct() - petition.getQuantityReserved());

                Reserved reservedItem = new Reserved();
                long startDateMillis = System.currentTimeMillis();
                long expirationDateMillis = startDateMillis;
                String code = startDateMillis + product.getCodeProduct() + "ONLINE";

                reservedItem.setStartDate(new Date(startDateMillis));
                reservedItem.setExpirationDate(new Date(expirationDateMillis));
                reservedItem.setCodeReserved(code);
                reservedItem.setProduct(product);
                reservedItem.setQuantityReserved(petition.getQuantityReserved());
                reservedItem.setUserEntity(currentUser);
                reservedItem.setActiveReserved(true);

                order.addReservedItem(reservedItem);
                preferenceItems.add(PreferenceItemRequest.builder()
                        .id(product.getCodeProduct())
                        .title(product.getProductName())
                        .quantity(petition.getQuantityReserved().intValue())
                        .currencyId("CLP")
                        .unitPrice(new BigDecimal(product.getPriceProduct().toString()))
                        .build());
            }
            MercadoPagoConfig.setAccessToken(token);
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(frontendBaseUrl + redirectionPath)
                    .pending(frontendBaseUrl + redirectionPath)
                    .failure(frontendBaseUrl + redirectionPath)
                    .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(preferenceItems)
                    .backUrls(backUrls)
                    .notificationUrl(backendBaseUrl + "/MyMDentalCommerce/pay/webhook")
                    .externalReference(order.getId())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            order.setMercadoPagoPreferenceId(preference.getId());
            orderRepository.save(order);
            logger.info("New Order created");
            return preference.getId();
        } catch (MPApiException ex){
            System.err.println("Error de la API de Mercado Pago - Status: " + ex.getStatusCode());
            System.err.println("Error de la API de Mercado Pago - Response: " + ex.getApiResponse().getContent());
            throw ex;
        }
    }

    private UserEntity findAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userEntityRepository.findByEmailUser(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundEntityException(
                            ExceptionValues.USER_NOT_FOUND_CODE,
                            Entities.USER_ENTITY,
                            ExceptionValues.USER_NOT_FOUND_MESSAGE
                    ));
        }
        throw new NullValuesEntityException(
                ExceptionValues.NOT_USER_CREDENTIALS_CODE,
                Entities.USER_ENTITY,
                ExceptionValues.NOT_USER_CREDENTIALS_MESSAGE
        );
    }

    @Transactional
    public void processWebhookNotification(String paymentId) {
        try {
            MercadoPagoConfig.setAccessToken(token);

            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            String orderId = payment.getExternalReference();
            if (orderId == null) {
                System.err.println("Error: El pago " + paymentId + " no tiene una external_reference.");
                return;
            }
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.ORDER_NOT_FOUND_CODE, Entities.ORDER, ExceptionValues.ORDER_NOT_FOUND_MESSAGE));

            switch (payment.getStatus()) {
                case "approved":
                    logger.fine("Order approved");
                    order.setStatus(OrderStatus.PAID);
                    order.setMercadoPagoPaymentId(payment.getId().toString());
                    break;
                case "rejected":
                case "cancelled":
                case "failure":
                    logger.warning("Order failure");
                    order.setStatus(OrderStatus.FAILED);
                    replenishStockForOrder(order);
                    break;
                case "in_process":
                case "pending":
                    logger.info("Order pending");
                    order.setStatus(OrderStatus.PENDING);
                    break;
            }
            orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("Error procesando el webhook: " + e.getMessage());
        }
    }

    private void replenishStockForOrder(Order order) {
        for (Reserved reservedItem : order.getReservedItems()) {
            Product product = reservedItem.getProduct();
            long quantityToReturn = reservedItem.getQuantityReserved();
            product.setStockProduct(product.getStockProduct() + quantityToReturn);
            productRepository.save(product);
        }
    }
}