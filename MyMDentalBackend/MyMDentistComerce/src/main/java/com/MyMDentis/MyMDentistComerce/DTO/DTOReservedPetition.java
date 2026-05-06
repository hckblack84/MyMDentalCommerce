package com.MyMDentis.MyMDentistComerce.DTO;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOReservedPetition {

    private Long idReserved;
    private String codeReserved;
    private Long quantityReserved;
    private Long idProduct;
}
