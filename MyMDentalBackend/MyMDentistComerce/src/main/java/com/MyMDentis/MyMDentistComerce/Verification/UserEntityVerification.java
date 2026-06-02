package com.MyMDentis.MyMDentistComerce.Verification;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.Roles;

import java.util.regex.Pattern;

public class UserEntityVerification {

    private final String patternName = "^[a-zA-ZÀ-ÿ]+$";

    final String NAME_USER = "Nombre del usuario";
    final String SURNAME_USER = "Apellido del usuario";
    final String EMAIL_USER = "Correo electronico del usuario";
    final String PASSWORD_USER = "Contraseña del usuario";
    final String CELLPHONE_USER = "Telefono/Celular del usuario";
    final String ROLE_USER = "Rol del usuario";

    public String nullCreateUser(DTOUserEntity dtoUserEntity) {
        if (nullNameUser(dtoUserEntity.getNameUser())) {
            return NAME_USER;
        }
        if (nullSurnameUser(dtoUserEntity.getSurnameUser())) {
            return SURNAME_USER;
        }
        if (nullEmailUser(dtoUserEntity.getEmailUser())) {
            return EMAIL_USER;
        }
        if (nullPasswordUser(dtoUserEntity.getPasswordUser())) {
            return PASSWORD_USER;
        }
        if (nullRoleUser(dtoUserEntity.getRole())) {
            return ROLE_USER;
        }
        return null;
    }

    public String nullCreateDefaultUser(DTOUserEntity dtoUserEntity){
        if (nullNameUser(dtoUserEntity.getNameUser())) {
            return NAME_USER;
        }
        if (nullSurnameUser(dtoUserEntity.getSurnameUser())) {
            return SURNAME_USER;
        }
        if (nullEmailUser(dtoUserEntity.getEmailUser())) {
            return EMAIL_USER;
        }
        if (nullPasswordUser(dtoUserEntity.getPasswordUser())) {
            return PASSWORD_USER;
        }
        return null;
    }

    public String nullCredentials(DTOCredentials dtoCredentials){
        if (nullEmailUser(dtoCredentials.getEmailUser())){
            return EMAIL_USER;
        }
        if (nullPasswordUser(dtoCredentials.getPassword())){
            return PASSWORD_USER;
        }
        return null;
    }

    public String validUserEntityValues(DTOUserEntity dtoUserEntity){
        if (invalidNameUser(dtoUserEntity.getNameUser())){
            return NAME_USER;
        }
        if (invalidSurnameUser(dtoUserEntity.getSurnameUser())){
            return SURNAME_USER;
        }
        if (invalidEmailUser(dtoUserEntity.getEmailUser())){
            return EMAIL_USER;
        }
        if (invalidPasswordUser(dtoUserEntity.getPasswordUser())){
            return PASSWORD_USER;
        }
        if (invalidCellphoneUser(dtoUserEntity.getCellphoneUser())){
            return CELLPHONE_USER;
        }
        if (!validRoleUser(dtoUserEntity.getRole())){
            return ROLE_USER;
        }
        return null;
    }

    public String validDefaultUserEntityValues(DTOUserEntity dtoUserEntity){
        if (invalidNameUser(dtoUserEntity.getNameUser())){
            return NAME_USER;
        }
        if (invalidSurnameUser(dtoUserEntity.getSurnameUser())){
            return SURNAME_USER;
        }
        if (invalidEmailUser(dtoUserEntity.getEmailUser())){
            return EMAIL_USER;
        }
        if (invalidPasswordUser(dtoUserEntity.getPasswordUser())){
            return PASSWORD_USER;
        }
        if (invalidCellphoneUser(dtoUserEntity.getCellphoneUser())){
            return CELLPHONE_USER;
        }
        return null;
    }

    public String validCredentialsValues(DTOCredentials dtoCredentials){
        if (invalidEmailUser(dtoCredentials.getEmailUser())){
            return EMAIL_USER;
        }
        if (invalidPasswordUser(dtoCredentials.getPassword())){
            return PASSWORD_USER;
        }
        return null;
    }

    //basic validations

    public boolean invalidNameUser(String nameUser){
        return nameUser.trim().length() > 25 ||
                nameUser.trim().length() <= 3 ||
                !Pattern.compile(patternName).matcher(nameUser).matches();
    }

    public boolean invalidSurnameUser(String surnameUser){
        return surnameUser.trim().length() > 25 ||
                surnameUser.trim().length() <= 3 ||
                !Pattern.compile(patternName).matcher(surnameUser).matches();
    }

    public boolean invalidEmailUser(String emailUser){
        String patternEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return emailUser.trim().length() <= 5 ||
                emailUser.trim().length() > 100 ||
                !Pattern.compile(patternEmail).matcher(emailUser).matches();
    }

    public boolean invalidPasswordUser(String passwordUser){
        return passwordUser.trim().length() < 8 ||
                passwordUser.trim().length() > 15;
    }

    public boolean invalidCellphoneUser(Long cellphoneUser){
        if (cellphoneUser == null) return false; // si es nulo, se considera valido porque no es un campo obligatorio si en nullCreateUser no se pide. Si fuera obligatorio, se validaria en nullCreateUser. Pero devolvemos false (no es invalido) para evitar el NullPointerException. Si es obligatorio, se devuelve true. Suponiendo que es opcional:
        return Long.toString(cellphoneUser).trim().length() != 9;
    }

    public boolean validRoleUser(Roles roleUser){
        if (roleUser == null) return false;
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