package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOJwt;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import com.MyMDentis.MyMDentistComerce.Security.JwtService;
import com.MyMDentis.MyMDentistComerce.Service.UserEntityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/MyMDentalCommerce/session")
public class AuthController {

    private final UserEntityService userEntityService;
    private final UserEntityRepository userEntityRepository;

    public AuthController(UserEntityService userEntityService, UserEntityRepository userEntityRepository) {
        this.userEntityService = userEntityService;
        this.userEntityRepository = userEntityRepository;
    }


    @PostMapping(path = "/register")
    public ResponseEntity<DTOUserEntity> registerUser(@RequestBody DTOUserEntity dtoUserEntity) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.createUser(dtoUserEntity));

    }

    @PostMapping(path = "/login")
    public ResponseEntity<DTOJwt> sessionUser(
            @RequestBody DTOCredentials dtoCredentials,
            HttpServletResponse response) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.sessionUser(dtoCredentials, response));
    }


}
