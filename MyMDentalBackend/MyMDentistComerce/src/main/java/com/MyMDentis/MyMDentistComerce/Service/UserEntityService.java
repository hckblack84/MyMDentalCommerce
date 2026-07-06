package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOJwt;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Exception.ExceptionValues;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NullValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Roles;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import com.MyMDentis.MyMDentistComerce.Security.JwtService;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import com.MyMDentis.MyMDentistComerce.Verification.UserEntityVerification;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    private final CookieService cookieService;
    private final DTOUserEntity dtoUserEntity = new DTOUserEntity();
    private final UserEntityVerification userEntityVerification = new UserEntityVerification();

    public UserEntityService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    public List<DTOUserEntity> getAllUsers() {
        List<UserEntity> users = userEntityRepository.findAll();
        return dtoUserEntity.parseDTOUserEntityList(users);
    }

    public DTOUserEntity findUserByUsername(String username) {
        UserEntity user = userEntityRepository.findByNameUser(username).orElseThrow(() ->
                new NotFoundEntityException(ExceptionValues.USER_NOT_FOUND_CODE,
                        Entities.USER_ENTITY,
                        ExceptionValues.USER_NOT_FOUND_MESSAGE));
        return dtoUserEntity.parseDTOUserEntity(user);
    }

    public DTOUserEntity createUser(DTOUserEntity dtoUserEntity) {

        String entity = userEntityVerification.nullCreateUser(dtoUserEntity);
        if (entity != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, entity, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        entity = userEntityVerification.validUserEntityValues(dtoUserEntity);
        if (entity != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, entity, ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        if (entityExist(dtoUserEntity.getNameUser(), dtoUserEntity.getSurnameUser())){
            throw new InvalidValuesEntityException(ExceptionValues.USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.USER_ALREADY_EXIST_MESSAGE);
        }

        if (emailRegistered(dtoUserEntity.getEmailUser())){
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        UserEntity newUser = UserEntity.builder()
                .nameUser(dtoUserEntity.getNameUser())
                .surnameUser(dtoUserEntity.getSurnameUser())
                .cellphoneUser(dtoUserEntity.getCellphoneUser())
                .passwordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()))
                .emailUser(dtoUserEntity.getEmailUser())
                .role(dtoUserEntity.getRole())
                .build();
        return dtoUserEntity.parseDTOUserEntity(userEntityRepository.save(newUser));
    }

    public DTOUserEntity createDefaultUser(DTOUserEntity dtoUserEntity) {
        String entity = userEntityVerification.nullCreateDefaultUser(dtoUserEntity);
        if (entity != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, entity, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        entity = userEntityVerification.validDefaultUserEntityValues(dtoUserEntity);
        if (entity != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, entity, ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        if (entityExist(dtoUserEntity.getNameUser(), dtoUserEntity.getSurnameUser())){
            throw new InvalidValuesEntityException(ExceptionValues.USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.USER_ALREADY_EXIST_MESSAGE);
        }

        if (emailRegistered(dtoUserEntity.getEmailUser())){
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        UserEntity newUser = UserEntity.builder()
                .nameUser(dtoUserEntity.getNameUser())
                .surnameUser(dtoUserEntity.getSurnameUser())
                .cellphoneUser(dtoUserEntity.getCellphoneUser())
                .passwordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()))
                .emailUser(dtoUserEntity.getEmailUser())
                .role(Roles.CLIENT)
                .build();
        return dtoUserEntity.parseDTOUserEntity(userEntityRepository.save(newUser));
    }

    public DTOJwt sessionUser(DTOCredentials dtoCredentials, HttpServletResponse response) {
        String exception = userEntityVerification.nullCredentials(dtoCredentials);
        if (exception != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, exception, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        exception = userEntityVerification.validCredentialsValues(dtoCredentials);
        if (exception != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, exception, ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoCredentials.getEmailUser(), dtoCredentials.getPassword())
            );
            if (authentication.isAuthenticated()) {
                UserEntity user = userEntityRepository.findByEmailUser(dtoCredentials.getEmailUser()).orElseThrow(
                        () -> new NotFoundEntityException(ExceptionValues.USER_NOT_FOUND_CODE, "Usuario", ExceptionValues.USER_NOT_FOUND_MESSAGE)
                );
                String token = jwtService.generateToken(user.getEmailUser(), user.getRole(), user.getNameUser());
                cookieService.addHttpOnlyCookie("jwt", token , 7*24*60*60, response);
                return DTOJwt.builder()
                        .useremail(user.getEmailUser())
                        .token(token)
                        .role(user.getRole())
                        .build();
            }
        } catch (BadCredentialsException e) {
            throw new NotFoundEntityException(ExceptionValues.USER_NOT_FOUND_CODE, "Session", ExceptionValues.USER_NOT_FOUND_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundEntityException(ExceptionValues.UNKNOWN_EXCEPTION_CODE, e.getMessage(), ExceptionValues.UNKNOW_EXCEPTION_MESSAGE);
        }
        throw new NotFoundEntityException(ExceptionValues.UNKNOWN_EXCEPTION_CODE, "Unknown", ExceptionValues.UNKNOW_EXCEPTION_MESSAGE);
    }

    public boolean emailRegistered(String emailUser) {
        Optional<UserEntity> user = userEntityRepository.findByEmailUser(emailUser);
        return user.isPresent();
    }


    public DTOUserEntity updateUserEntity(String email, DTOUserEntity dtoUserEntity) {
        UserEntity userExistente = userEntityRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Error: Usuario con email " + email + " no encontrado."));

        userExistente.setNameUser(dtoUserEntity.getNameUser());
        userExistente.setCellphoneUser(dtoUserEntity.getCellphoneUser());
        userExistente.setSurnameUser(dtoUserEntity.getSurnameUser());

        UserEntity saved = userEntityRepository.save(userExistente);

        DTOUserEntity response = new DTOUserEntity();
        response.setNameUser(saved.getNameUser());
        response.setSurnameUser(saved.getSurnameUser());
        response.setCellphoneUser(saved.getCellphoneUser());
        return response;
    }


    @Transactional
    public DTOUserEntity updateUser(String emailUser, DTOUserEntity dtoUserEntity, boolean withPassword) {

        if (userEntityVerification.nullEmailUser(emailUser)){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, "Correo electronico", ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        if (userEntityVerification.invalidEmailUser(emailUser)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, "Correo electronico", ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        String exception = userEntityVerification.nullCreateDefaultUser(dtoUserEntity);
        if (exception != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, exception, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        exception = userEntityVerification.validDefaultUserEntityValues(dtoUserEntity);
        if (exception != null) {
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, exception, ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        UserEntity user = userEntityRepository.findByEmailUser(emailUser)
                .orElseThrow(() -> new NotFoundEntityException(
                        ExceptionValues.USER_NOT_FOUND_CODE,
                        Entities.USER_ENTITY,
                        ExceptionValues.USER_NOT_FOUND_MESSAGE)
                );

        if (!user.getEmailUser().equals(dtoUserEntity.getEmailUser()) && emailRegistered(dtoUserEntity.getEmailUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        user.setNameUser(dtoUserEntity.getNameUser());
        user.setSurnameUser(dtoUserEntity.getSurnameUser());
        user.setCellphoneUser(dtoUserEntity.getCellphoneUser());
        user.setEmailUser(dtoUserEntity.getEmailUser());

        if (withPassword){
            user.setPasswordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()));
        }

        UserEntity updatedUser = userEntityRepository.save(user);
        return new DTOUserEntity().parseDTOUserEntity(updatedUser);
    }

    @Transactional
    public DTOUserEntity adminUpdateUser(String emailTarget, DTOUserEntity dtoUserEntity, boolean withPassword) {

        if (userEntityVerification.nullEmailUser(emailTarget)){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, "Correo electronico", ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        if (userEntityVerification.invalidEmailUser(emailTarget)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, "Correo electronico", ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        String exception = userEntityVerification.nullCreateUser(dtoUserEntity);
        if (exception != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, exception, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        exception = userEntityVerification.validUserEntityValues(dtoUserEntity);
        if (exception != null) {
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE, exception, ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }

        UserEntity user = userEntityRepository.findByEmailUser(emailTarget)
                .orElseThrow(() -> new NotFoundEntityException(
                        ExceptionValues.USER_NOT_FOUND_CODE,
                        Entities.USER_ENTITY,
                        ExceptionValues.USER_NOT_FOUND_MESSAGE)
                );

        if (!user.getEmailUser().equals(dtoUserEntity.getEmailUser()) && emailRegistered(dtoUserEntity.getEmailUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, Entities.USER_ENTITY, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        user.setNameUser(dtoUserEntity.getNameUser());
        user.setSurnameUser(dtoUserEntity.getSurnameUser());
        user.setCellphoneUser(dtoUserEntity.getCellphoneUser());
        user.setEmailUser(dtoUserEntity.getEmailUser());
        user.setPasswordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()));
        user.setRole(dtoUserEntity.getRole());

        UserEntity updatedUser = userEntityRepository.save(user);
        return new DTOUserEntity().parseDTOUserEntity(updatedUser);

    }

    public boolean entityExist(String nameUser, String surnameUser) {
        Optional<UserEntity> user = userEntityRepository.findByNameUserAndSurnameUser(nameUser, surnameUser);
        return user.isPresent();
    }

    @Transactional
    public DTOUserEntity findByemailUser(String email) {

        UserEntity user = userEntityRepository.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("USUARIO NO ENCONTRADO"));

        DTOUserEntity dtouser = new DTOUserEntity();
        dtouser.setNameUser(user.getNameUser());
        dtouser.setSurnameUser(user.getSurnameUser());
        dtouser.setEmailUser(user.getEmailUser());
        dtouser.setCellphoneUser(user.getCellphoneUser());
        return dtouser;
    }

    public void logout(HttpServletResponse response) {
        cookieService.deleteCookie("jwt", response);

    }




}