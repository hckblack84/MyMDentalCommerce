package com.MyMDentis.MyMDentistComerce.Exception;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTONullException {

    private String code;
    private String message;

}
