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
import com.MyMDentis.MyMDentistComerce.Verification.UserEntityAtributes;
import com.MyMDentis.MyMDentistComerce.Verification.UserEntityVerification;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserEntityService implements UserEntityAtributes {

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
        //return userEntityRepository.findAll().stream().map(dtoUserEntity::parseDTOUserEntity).toList();
        return dtoUserEntity.parseDTOUserEntityList(users);
    }

    public DTOUserEntity findUserByUsername(String username) {

        UserEntity user = userEntityRepository.findByNameUser(username).orElseThrow(() ->
                new NotFoundEntityException(ExceptionValues.USER_NOT_FOUND_CODE, Entities.USER_ENTITY, ExceptionValues.USER_NOT_FOUND_MESSAGE));

        return dtoUserEntity.parseDTOUserEntity(Objects.requireNonNull(userEntityRepository.findByNameUser(username).orElse(null)));
    }

    public DTOUserEntity createUser(DTOUserEntity dtoUserEntity) {
        if (userEntityVerification.validNullsUserEntity(dtoUserEntity)) {
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        String exception = userEntityVerification.validUserEntityValues(dtoUserEntity);

        if (exception != null) {
            throw new InvalidValuesEntityException(ExceptionValues.USER_REGISTER_INVALID_CODE, exception, ExceptionValues.USER_REGISTER_INVALID_MESSAGE);
        }

        if (entityExist(dtoUserEntity.getNameUser(), dtoUserEntity.getSurnameUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.USER_ALREADY_EXIST_CODE, NAME_USER + " / " + SURNAME_USER, ExceptionValues.USER_ALREADY_EXIST_MESSAGE);
        }

        if (emailRegistered(dtoUserEntity.getEmailUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, EMAIL_USER, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        UserEntity user = UserEntity.builder()
                .cellphoneUser(dtoUserEntity.getCellphoneUser())
                .emailUser(dtoUserEntity.getEmailUser())
                .nameUser(dtoUserEntity.getNameUser())
                .surnameUser(dtoUserEntity.getSurnameUser())
                .passwordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()))
                .role(dtoUserEntity.getRole())
                .build();
        return dtoUserEntity.parseDTOUserEntity(userEntityRepository.save(user));
    }

    public DTOUserEntity createDefaultUser(DTOUserEntity dtoUserEntity) {

        //if (userEntityVerification.validNullsUserEntity(dtoUserEntity)) {
        //    throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        //}

        //String exception = userEntityVerification.validUserEntityValues(dtoUserEntity);

        //if (exception != null) {
        //    throw new InvalidValuesEntityException(ExceptionValues.USER_REGISTER_INVALID_CODE, exception, ExceptionValues.USER_REGISTER_INVALID_MESSAGE);
        //}

        if (entityExist(dtoUserEntity.getNameUser(), dtoUserEntity.getSurnameUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.USER_ALREADY_EXIST_CODE, NAME_USER + " / " + SURNAME_USER, ExceptionValues.USER_ALREADY_EXIST_MESSAGE);
        }

        if (emailRegistered(dtoUserEntity.getEmailUser())) {
            throw new InvalidValuesEntityException(ExceptionValues.EMAIL_USER_ALREADY_EXIST_CODE, EMAIL_USER, ExceptionValues.EMAIL_USER_ALREADY_EXIST_MESSAGE);
        }

        UserEntity user = UserEntity.builder()
                .cellphoneUser(dtoUserEntity.getCellphoneUser())
                .emailUser(dtoUserEntity.getEmailUser())
                .nameUser(dtoUserEntity.getNameUser())
                .passwordUser(passwordEncoder.encode(dtoUserEntity.getPasswordUser()))
                .role(Roles.CLIENT)
                .build();

        return dtoUserEntity.parseDTOUserEntity(userEntityRepository.save(user));
    }

    public DTOJwt sessionUser(DTOCredentials dtoCredentials, HttpServletResponse response) {
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
                        .username(user.getNameUser())
                        .useremail(user.getEmailUser())
                        .usercellphone(user.getCellphoneUser())
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



    @Transactional
    public DTOUserEntity updateUser(String emailUser, DTOUserEntity dtoUserEntity) {

        UserEntity user = userEntityRepository.findByEmailUser(emailUser)
                .orElseThrow(() -> new NotFoundEntityException(
                        ExceptionValues.USER_NOT_FOUND_CODE,
                        "Usuario",
                        ExceptionValues.USER_NOT_FOUND_MESSAGE)
                );


        if (dtoUserEntity.getNameUser() != null) user.setNameUser(dtoUserEntity.getNameUser());
        if (dtoUserEntity.getSurnameUser() != null) user.setSurnameUser(dtoUserEntity.getSurnameUser());
        if (dtoUserEntity.getCellphoneUser() != null) user.setCellphoneUser(dtoUserEntity.getCellphoneUser());
        if (dtoUserEntity.getEmailUser() != null) user.setEmailUser(dtoUserEntity.getEmailUser());
        if (dtoUserEntity.getPasswordUser() != null) {

            user.setPasswordUser(dtoUserEntity.getPasswordUser());
        }


        UserEntity updatedUser = userEntityRepository.save(user);

        return new DTOUserEntity().parseDTOUserEntity(updatedUser);
    }
    @Transactional
    public DTOUserEntity adminUpdate(String emailTarget, DTOUserEntity dtoUserEntity) {
        UserEntity user = userEntityRepository.findByEmailUser(emailTarget)
                .orElseThrow(() -> new NotFoundEntityException(
                        ExceptionValues.USER_NOT_FOUND_CODE,
                        "Usuario no encontrado",
                        ExceptionValues.USER_NOT_FOUND_MESSAGE)
                );
        if (dtoUserEntity.getNameUser() != null) user.setNameUser(dtoUserEntity.getNameUser());
        if (dtoUserEntity.getSurnameUser() != null) user.setSurnameUser(dtoUserEntity.getSurnameUser());
        if (dtoUserEntity.getCellphoneUser() != null) user.setCellphoneUser(dtoUserEntity.getCellphoneUser());

        if (dtoUserEntity.getEmailUser() != null) {
            user.setEmailUser(dtoUserEntity.getEmailUser());
        }

        if (dtoUserEntity.getPasswordUser() != null) {
            user.setPasswordUser(dtoUserEntity.getPasswordUser());
        }

        UserEntity updatedUser = userEntityRepository.save(user);
        return new DTOUserEntity().parseDTOUserEntity(updatedUser);

    }

    public boolean entityExist(String nameUser, String surnameUser) {
        Optional<UserEntity> user = userEntityRepository.findByNameUserAndSurnameUser(nameUser, surnameUser);
        return user.isPresent();
    }
}
