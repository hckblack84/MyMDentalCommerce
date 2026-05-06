package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReserved;
import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Exception.ExceptionValues;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NullValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.ReservedRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReservedService {

    @Autowired
    private ReservedRepository reservedRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private ProductRepository productRepository;


    public List<Reserved> getAllOrders(){
        return reservedRepository.findAll();
    }

    public Reserved findOrderById(Long id){
        return reservedRepository.findById(id).orElseThrow(() -> new RuntimeException("xd"));
    }

    public List<Reserved> findByUser(Long idUserEntity){

        UserEntity user = userEntityRepository.findById(idUserEntity).orElseThrow(
                () -> new RuntimeException("xd1")
        );

        return reservedRepository.findByUserEntity(user);
    }

    public List<Reserved> findActivesOrders(){
        return reservedRepository.findByActiveReserved(true);
    }

    public List<Reserved> findNoActivesOrders(){
        return reservedRepository.findByActiveReserved(false);
    }

    @Transactional
    public List<DTOReserved> saveNewOrder(List<DTOReservedPetition> petitions){

        DTOReserved dtoReserved = new DTOReserved();
        List<DTOReserved> reservedList = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&authentication.getPrincipal() != null){
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails){
                UserEntity user = userEntityRepository.findByEmailUser(((UserDetails) principal).getUsername())
                        .orElseThrow(() -> new NotFoundEntityException(
                                ExceptionValues.USER_NOT_FOUND_CODE,
                                Entities.USER_ENTITY,
                                ExceptionValues.USER_NOT_FOUND_MESSAGE
                        ));

                for (DTOReservedPetition petition : petitions){
                    Product product = findProduct(petition.getIdProduct());
                    if (product.getStockProduct() - petition.getQuantityReserved() < 0){
                        throw new InvalidValuesEntityException(
                                ExceptionValues.INVALID_STOCK_REQUEST_CODE,
                                Entities.PRODUCT,
                                ExceptionValues.INVALID_STOCK_REQUEST_MESSAGE
                        );
                    }

                    product.setStockProduct(product.getStockProduct() - petition.getQuantityReserved());
                    productRepository.save(product);

                    long expirationReserved = 1000L * 60 * 60 * 24 * 2;
                    long startDate = new Date().getTime();
                    long expirationDate = new Date(System.currentTimeMillis() + expirationReserved).getTime();
                    String code = startDate + product.getCodeProduct() + expirationReserved;
                    Reserved reserved = reservedRepository.save(
                            Reserved.builder()
                                    .codeReserved(code)
                                    .activeReserved(true)
                                    .quantityReserved(petition.getQuantityReserved())
                                    .product(product)
                                    .userEntity(user)
                                    .startDate(new Date(startDate))
                                    .expirationDate(new Date(expirationDate))
                                    .build()
                    );
                    reservedList.add(dtoReserved.parseDTOOrder(reserved));
                }
            }else {
                throw new NotFoundEntityException(ExceptionValues.VALUES_NOT_COMPATIBLE_REQUEST_CODE,
                        Entities.USER_ENTITY,
                        ExceptionValues.VALUES_NOT_COMPATIBLE_REQUEST_MESSAGE);
            }

        }else{
            throw new NullValuesEntityException(
                    ExceptionValues.NOT_USER_CREDENTIALS_CODE,
                    ExceptionValues.NOT_USER_CREDENTIALS_MESSAGE
            );
        }
        return reservedList;
    }

    /////////////////////////////////////UTILS/////////////////////////////
    private Product findProduct(Long idProduct){
        return productRepository.findById(idProduct).orElseThrow(
                () -> new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE,
                        Entities.PRODUCT,
                        ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE)
        );

    }
}
