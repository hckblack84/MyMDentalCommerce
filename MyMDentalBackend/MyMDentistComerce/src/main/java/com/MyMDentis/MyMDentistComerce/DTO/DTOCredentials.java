package com.MyMDentis.MyMDentistComerce.DTO;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOCredentials {

    private String emailUser;
    private String password;

}
