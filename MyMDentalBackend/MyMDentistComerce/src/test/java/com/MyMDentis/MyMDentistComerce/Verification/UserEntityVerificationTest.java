package com.MyMDentis.MyMDentistComerce.Verification;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityVerificationTest {

    private UserEntityVerification userEntityVerification;
    private DTOUserEntity dtoUserEntity;
    private DTOCredentials dtoCredentials;

    @BeforeEach
    void setUp() {
        userEntityVerification = new UserEntityVerification();
        dtoUserEntity = new DTOUserEntity("MockName", "MockeSurname", "mock@test.com", "mockPassword", 123456789L, Roles.CLIENT);
        dtoCredentials = new DTOCredentials("mock@test.com", "mockPassword");
    }

    //null tests

    @Test
    @DisplayName("Null Validation: nullCreateUser should return field name for null values")
    void nullCreateUser_shouldReturnFieldNameForNullValues() {
        dtoUserEntity.setNameUser(null);
        assertEquals(userEntityVerification.NAME_USER, userEntityVerification.nullCreateUser(dtoUserEntity));
        dtoUserEntity.setNameUser("Mock");

        dtoUserEntity.setSurnameUser(null);
        assertEquals(userEntityVerification.SURNAME_USER, userEntityVerification.nullCreateUser(dtoUserEntity));
        dtoUserEntity.setSurnameUser("MockSurname");

        dtoUserEntity.setEmailUser(null);
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.nullCreateUser(dtoUserEntity));
        dtoUserEntity.setEmailUser("mock@test.com");

        dtoUserEntity.setPasswordUser(null);
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.nullCreateUser(dtoUserEntity));
        dtoUserEntity.setPasswordUser("mockPassword");

        dtoUserEntity.setRole(null);
        assertEquals(userEntityVerification.ROLE_USER, userEntityVerification.nullCreateUser(dtoUserEntity));
        dtoUserEntity.setRole(Roles.CLIENT);

        assertNull(userEntityVerification.nullCreateUser(dtoUserEntity));
    }

    @Test
    @DisplayName("Null Validation: nullCreateDefaultUser should return field name for null values")
    void nullCreateDefaultUser_shouldReturnFieldNameForNullValues() {
        dtoUserEntity.setNameUser(null);
        assertEquals(userEntityVerification.NAME_USER, userEntityVerification.nullCreateDefaultUser(dtoUserEntity));
        dtoUserEntity.setNameUser("Mock");

        dtoUserEntity.setSurnameUser(null);
        assertEquals(userEntityVerification.SURNAME_USER, userEntityVerification.nullCreateDefaultUser(dtoUserEntity));
        dtoUserEntity.setSurnameUser("MockSurname");

        dtoUserEntity.setEmailUser(null);
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.nullCreateDefaultUser(dtoUserEntity));
        dtoUserEntity.setEmailUser("mock@test.com");

        dtoUserEntity.setPasswordUser(null);
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.nullCreateDefaultUser(dtoUserEntity));
        dtoUserEntity.setPasswordUser("mockPassword");

        assertNull(userEntityVerification.nullCreateDefaultUser(dtoUserEntity));
    }

    @Test
    @DisplayName("Null Validation: nullCredentials should return field name for null values")
    void nullCredentials_shouldReturnFieldNameForNullValues() {
        dtoCredentials.setEmailUser(null);
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.nullCredentials(dtoCredentials));
        dtoCredentials.setEmailUser("mock@test.com");

        dtoCredentials.setPassword(null);
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.nullCredentials(dtoCredentials));
        dtoCredentials.setPassword("mockPassword");

        assertNull(userEntityVerification.nullCredentials(dtoCredentials));
    }

    //Invalid values tests

    @Test
    @DisplayName("Value Validation: validUserEntityValues should return field name for invalid values")
    void validUserEntityValues_shouldReturnFieldNameForInvalidValues() {
        dtoUserEntity.setNameUser("a");
        assertEquals(userEntityVerification.NAME_USER, userEntityVerification.validUserEntityValues(dtoUserEntity));
        dtoUserEntity.setNameUser("Mock");

        dtoUserEntity.setSurnameUser("a");
        assertEquals(userEntityVerification.SURNAME_USER, userEntityVerification.validUserEntityValues(dtoUserEntity));
        dtoUserEntity.setSurnameUser("MockSurname");

        dtoUserEntity.setEmailUser("invalid-email");
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.validUserEntityValues(dtoUserEntity));
        dtoUserEntity.setEmailUser("mock@test.com");

        dtoUserEntity.setPasswordUser("aei");
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.validUserEntityValues(dtoUserEntity));
        dtoUserEntity.setPasswordUser("mockPassword");

        dtoUserEntity.setCellphoneUser(123L);
        assertEquals(userEntityVerification.CELLPHONE_USER, userEntityVerification.validUserEntityValues(dtoUserEntity));
        dtoUserEntity.setCellphoneUser(123456789L);

        assertNull(userEntityVerification.validUserEntityValues(dtoUserEntity));
    }

    @Test
    @DisplayName("Value Validation: validDefaultUserEntityValues should return field name for invalid values")
    void validDefaultUserEntityValues_shouldReturnFieldNameForInvalidValues() {
         dtoUserEntity.setNameUser("a");
        assertEquals(userEntityVerification.NAME_USER, userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
        dtoUserEntity.setNameUser("Mock");

        dtoUserEntity.setSurnameUser("a");
        assertEquals(userEntityVerification.SURNAME_USER, userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
        dtoUserEntity.setSurnameUser("MockSurname");

        dtoUserEntity.setEmailUser("invalid-email");
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
        dtoUserEntity.setEmailUser("mock@test.com");

        dtoUserEntity.setPasswordUser("short");
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
        dtoUserEntity.setPasswordUser("mockPassword");

        dtoUserEntity.setCellphoneUser(123L);
        assertEquals(userEntityVerification.CELLPHONE_USER, userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
        dtoUserEntity.setCellphoneUser(123456789L);

        assertNull(userEntityVerification.validDefaultUserEntityValues(dtoUserEntity));
    }

    @Test
    @DisplayName("Value Validation: validCredentialsValues should return field name for invalid values")
    void validCredentialsValues_shouldReturnFieldNameForInvalidValues() {
        dtoCredentials.setEmailUser("invalid-email");
        assertEquals(userEntityVerification.EMAIL_USER, userEntityVerification.validCredentialsValues(dtoCredentials));
        dtoCredentials.setEmailUser("mock@test.com");

        dtoCredentials.setPassword("aea");
        assertEquals(userEntityVerification.PASSWORD_USER, userEntityVerification.validCredentialsValues(dtoCredentials));
        dtoCredentials.setPassword("mockPassword");

        assertNull(userEntityVerification.validCredentialsValues(dtoCredentials));
    }

    //individual values tests

    @Test
    @DisplayName("Individual: invalidNameUser should return true for invalid names")
    void invalidNameUser_shouldReturnTrueForInvalidNames() {
        assertTrue(userEntityVerification.invalidNameUser("aa"));
        assertTrue(userEntityVerification.invalidNameUser("aaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertTrue(userEntityVerification.invalidNameUser("123"));
        assertTrue(userEntityVerification.invalidNameUser(""));
        assertTrue(userEntityVerification.invalidNameUser("  "));
        assertFalse(userEntityVerification.invalidNameUser("ValidName"));
    }

    @Test
    @DisplayName("Individual: invalidSurnameUser should return true for invalid surnames")
    void invalidSurnameUser_shouldReturnTrueForInvalidSurnames() {
        assertTrue(userEntityVerification.invalidSurnameUser("aa"));
        assertTrue(userEntityVerification.invalidSurnameUser("aaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertTrue(userEntityVerification.invalidSurnameUser("123"));
        assertTrue(userEntityVerification.invalidSurnameUser(""));
        assertTrue(userEntityVerification.invalidSurnameUser("  "));
        assertFalse(userEntityVerification.invalidSurnameUser("ValidSurname"));
    }

    @Test
    @DisplayName("Individual: invalidEmailUser should return true for invalid emails")
    void invalidEmailUser_shouldReturnTrueForInvalidEmails() {
        assertTrue(userEntityVerification.invalidEmailUser("test"));
        assertTrue(userEntityVerification.invalidEmailUser("test@"));
        assertTrue(userEntityVerification.invalidEmailUser("test@test"));
        assertTrue(userEntityVerification.invalidEmailUser("test@test."));
        assertTrue(userEntityVerification.invalidEmailUser(""));
        assertTrue(userEntityVerification.invalidEmailUser("  "));
        assertFalse(userEntityVerification.invalidEmailUser("test@test.com"));
    }

    @Test
    @DisplayName("Individual: invalidPasswordUser should return true for invalid passwords")
    void invalidPasswordUser_shouldReturnTrueForInvalidPasswords() {
        assertTrue(userEntityVerification.invalidPasswordUser("1234567"));
        assertTrue(userEntityVerification.invalidPasswordUser("1234567890123456"));
        assertTrue(userEntityVerification.invalidPasswordUser(""));
        assertTrue(userEntityVerification.invalidPasswordUser("   "));
        assertFalse(userEntityVerification.invalidPasswordUser("password"));
    }

    @Test
    @DisplayName("Individual: invalidCellphoneUser should return true for invalid cellphones")
    void invalidCellphoneUser_shouldReturnTrueForInvalidCellphones() {
        assertTrue(userEntityVerification.invalidCellphoneUser(12345678L));
        assertTrue(userEntityVerification.invalidCellphoneUser(1234567890L));
        assertFalse(userEntityVerification.invalidCellphoneUser(123456789L));
        assertFalse(userEntityVerification.invalidCellphoneUser(null));
    }

    @Test
    @DisplayName("Individual: validRoleUser should return false for invalid roles")
    void validRoleUser_shouldReturnFalseForInvalidRoles() {
        assertFalse(userEntityVerification.validRoleUser(null));
    }

    @Test
    @DisplayName("Individual: validRoleUser should return true for valid roles")
    void validRoleUser_shouldReturnTrueForValidRoles() {
        assertTrue(userEntityVerification.validRoleUser(Roles.CLIENT));
        assertTrue(userEntityVerification.validRoleUser(Roles.ADMINISTRATOR));
    }
}
