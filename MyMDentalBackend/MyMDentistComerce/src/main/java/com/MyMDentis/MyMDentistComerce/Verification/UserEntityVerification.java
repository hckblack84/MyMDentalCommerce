package com.MyMDentis.MyMDentistComerce.Verification;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.Roles;

import java.util.regex.Pattern;

public class UserEntityVerification implements UserEntityAtributes{

    private final String patternName = "^[a-zA-ZÀ-ÿ]+$";

    public boolean validNullsUserEntity(DTOUserEntity dtoUserEntity){
        return nullNameUser(dtoUserEntity.getNameUser()) ||
                nullSurnameUser(dtoUserEntity.getSurnameUser()) ||
                nullEmailUser(dtoUserEntity.getEmailUser()) ||
                nullPasswordUser(dtoUserEntity.getPasswordUser()) ||
                nullCellphoneUser(dtoUserEntity.getCellphoneUser()) ||
                nullRoleUser(dtoUserEntity.getRole());
    }

    public boolean validNullsCredentials(DTOCredentials dtoCredentials){
        return nullEmailUser(dtoCredentials.getEmailUser()) || nullPasswordUser(dtoCredentials.getPassword());
    }

    public String validUserEntityValues(DTOUserEntity dtoUserEntity){
        if (!validNameUser(dtoUserEntity.getNameUser())){
            return NAME_USER;
        }
        if (!validSurnameUser(dtoUserEntity.getSurnameUser())){
            return SURNAME_USER;
        }
        if (!validEmailUser(dtoUserEntity.getEmailUser())){
            return EMAIL_USER;
        }
        if (!validPasswordUser(dtoUserEntity.getPasswordUser())){
            return PASSWORD_USER;
        }
        if (!validCellphoneUser(dtoUserEntity.getCellphoneUser())){
            return CELLPHONE_USER;
        }
        if (!validRoleUser(dtoUserEntity.getRole())){
            return ROLE_USER;
        }
        return null;
    }

    public String validCredentialsValues(DTOCredentials dtoCredentials){
        if (!validEmailUser(dtoCredentials.getEmailUser())){
            return EMAIL_USER;
        }
        if (!validPasswordUser(dtoCredentials.getPassword())){
            return PASSWORD_USER;
        }
        return null;
    }

    //basic validations

    public boolean validNameUser(String nameUser){
        return nameUser.trim().length() <= 25 &&
                nameUser.trim().length() > 3 &&
                Pattern.compile(patternName).matcher(nameUser).matches();
    }

    public boolean validSurnameUser(String surnameUser){
        return surnameUser.trim().length() <= 25 &&
                surnameUser.trim().length() > 3 &&
                Pattern.compile(patternName).matcher(surnameUser).matches();
    }

    public boolean validEmailUser(String emailUser){
        String patternEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return emailUser.trim().length() > 5 &&
                emailUser.trim().length() <= 100 &&
                Pattern.compile(patternEmail).matcher(emailUser).matches();
    }

    public boolean validPasswordUser(String passwordUser){
        return passwordUser.trim().length() >= 8 &&
                passwordUser.trim().length() <= 15;
    }

    public boolean validCellphoneUser(Long cellphoneUser){
        return Long.toString(cellphoneUser).trim().length() == 9;
    }

    public boolean validRoleUser(Roles roleUser){
        for (Roles role : Roles.values()){
            if (role.name().equalsIgnoreCase(roleUser.name())){
                return true;
            }
        }
        return false;
    }

    // Null verifications

    public boolean nullNameUser(String nameUser){
        return nameUser == null || nameUser.trim().isEmpty();
    }

    public boolean nullSurnameUser(String surnameUser){
        return surnameUser == null || surnameUser.trim().isEmpty();
    }
    public boolean nullEmailUser(String emailUser){
        return emailUser == null || emailUser.trim().isEmpty();
    }
    public boolean nullPasswordUser(String passwordUser){
        return passwordUser == null || passwordUser.trim().isEmpty();
    }
    public boolean nullCellphoneUser(Long cellphoneUser){
        return cellphoneUser == null;
    }
    public boolean nullRoleUser(Roles role){
        return role == null;
    }

}
