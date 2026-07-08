package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Service.PaymentService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/MyMDentalCommerce/pay")
public class PaymentController {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/createOrder", produces = MediaType.TEXT_PLAIN_VALUE)
    public String createOrderAndPay(@RequestBody List<DTOReservedPetition> petitions) throws MPException, MPApiException {
        return paymentService.createOrderAndGetPaymentUrl(petitions);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> notification) {
        log.info("New webhook from mercado pago");

        if (notification.get("type").equals("payment")) {
            String paymentId = String.valueOf(notification.get("data.id"));
            paymentService.processWebhookNotification(paymentId);
        }
        return new ResponseEntity<>("Notification received", HttpStatus.OK);
    }
}
