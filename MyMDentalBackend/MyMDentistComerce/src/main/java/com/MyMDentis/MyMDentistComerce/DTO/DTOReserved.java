package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOReserved {

    private Long idReserved;
    private String codeReserved;
    private Long quantityReserved;
    private Long idProduct;
    private String productName;
    private Long priceProduct;
    private String emailUserEntity;
    private boolean activeReserved;

    private Date startDateReserved;
    private Date expirationDateReserved;

    public DTOReserved parseDTOOrder(Reserved reserved){
        return DTOReserved.builder()
                .idReserved(reserved.getIdReserved())
                .codeReserved(reserved.getCodeReserved())
                .quantityReserved(reserved.getQuantityReserved())
                .idProduct(reserved.getProduct().getIdProduct())
                .productName(reserved.getProduct().getProductName())
                .priceProduct(reserved.getProduct().getPriceProduct())
                .emailUserEntity(reserved.getUserEntity().getEmailUser())
                .activeReserved(reserved.isActiveReserved())
                .startDateReserved(reserved.getStartDate())
                .expirationDateReserved(reserved.getExpirationDate())
                .build();
    }

    public List<DTOReserved> parseDTOOrderList(List<Reserved> list) {
        
        List<DTOReserved> reservedList = new ArrayList<>();
        for (Reserved reserved : list) {
            reservedList.add(parseDTOOrder(reserved));
        }
        return reservedList;
    }
}


