package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import lombok.*;

import java.util.Date;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOReserved {

    private Long idReserved;
    private String codeReserved;
    private Long quantityReserved;
    private Long idProduct;
    private Long idUserEntity;
    private boolean activeReserved;

    private Date startDateReserved;
    private Date expirationDateReserved;

    public DTOReserved parseDTOOrder(Reserved reserved){
        return DTOReserved.builder()
                .idReserved(reserved.getIdReserved())
                .codeReserved(reserved.getCodeReserved())
                .quantityReserved(reserved.getQuantityReserved())
                .idProduct(reserved.getProduct().getIdProduct())
                .idUserEntity(reserved.getUserEntity().getIdUser())
                .startDateReserved(reserved.getStartDate())
                .expirationDateReserved(reserved.getExpirationDate())
                .build();
    }

}


