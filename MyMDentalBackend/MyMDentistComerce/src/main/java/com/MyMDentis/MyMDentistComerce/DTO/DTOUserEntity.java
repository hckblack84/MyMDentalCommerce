package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Roles;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOUserEntity {

    private String nameUser;
    private String surnameUser;
    private String emailUser;
    private String passwordUser;
    private Long cellphoneUser;
    private Roles role;

    public DTOUserEntity parseDTOUserEntity(UserEntity userEntity){
        return DTOUserEntity.builder()
                .nameUser(userEntity.getNameUser())
                .surnameUser(userEntity.getSurnameUser())
                .emailUser(userEntity.getEmailUser())
                .passwordUser(userEntity.getPasswordUser())
                .cellphoneUser(userEntity.getCellphoneUser())
                .role(userEntity.getRole())
                .build();
    }

    public List<DTOUserEntity> parseDTOUserEntityList(List<UserEntity> users) {

        List<DTOUserEntity> dtoUsers = new ArrayList<>();
        for (UserEntity user : users) {
            dtoUsers.add(parseDTOUserEntity(user));
        }
        return dtoUsers;
    }
}
