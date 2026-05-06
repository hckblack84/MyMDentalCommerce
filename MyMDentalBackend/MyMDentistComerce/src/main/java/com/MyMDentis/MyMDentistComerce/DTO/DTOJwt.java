package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Roles;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOJwt {

    private String username;
    private Roles role;
    private String token;
    private String useremail;
    private Long usercellphone;
}
