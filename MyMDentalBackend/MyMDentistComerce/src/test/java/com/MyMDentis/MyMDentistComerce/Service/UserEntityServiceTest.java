package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOCredentials;
import com.MyMDentis.MyMDentistComerce.DTO.DTOJwt;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Roles;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import com.MyMDentis.MyMDentistComerce.Security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private CookieService cookieService;

    private UserEntityService userEntityService;

    private DTOUserEntity dtoUser;
    private UserEntity userEntity;


    @BeforeEach
    void setUp() {
        userEntityService = new UserEntityService(cookieService);
        ReflectionTestUtils.setField(userEntityService, "userEntityRepository", userEntityRepository);
        ReflectionTestUtils.setField(userEntityService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(userEntityService, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(userEntityService, "jwtService", jwtService);
        dtoUser = new DTOUserEntity();
        dtoUser.setNameUser("Test");
        dtoUser.setSurnameUser("User");
        dtoUser.setEmailUser("test@user.com");
        dtoUser.setPasswordUser("password");
        dtoUser.setRole(Roles.ADMINISTRATOR);

        userEntity = new UserEntity();
        userEntity.setIdUser(1L);
        userEntity.setEmailUser("test@user.com");
        userEntity.setRole(Roles.CLIENT);
        userEntity.setNameUser("Name");
    }


    @Test
    @DisplayName("createUser create a user")
    void createUserValidUser() {
        when(userEntityRepository.findByNameUserAndSurnameUser(anyString(), anyString())).thenReturn(Optional.empty());
        when(userEntityRepository.findByEmailUser(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        DTOUserEntity result = userEntityService.createUser(dtoUser);

        assertNotNull(result);
        assertEquals("test@user.com", result.getEmailUser());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userEntityRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    @DisplayName("Exception from userEntity exist")
    void existUser() {
        when(userEntityRepository.findByNameUserAndSurnameUser(anyString(), anyString())).thenReturn(Optional.of(userEntity));
        assertThrows(InvalidValuesEntityException.class, () -> {
            userEntityService.createUser(dtoUser);
        });
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("createDefaultUser with Client Role")
    void createDefaultUser_shouldSaveWithClientRole() {
        when(userEntityRepository.findByNameUserAndSurnameUser(anyString(), anyString())).thenReturn(Optional.empty());
        when(userEntityRepository.findByEmailUser(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userEntityRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        DTOUserEntity result = userEntityService.createDefaultUser(dtoUser);

        assertNotNull(result);
        assertEquals(Roles.CLIENT, result.getRole(), "El rol devuelto debe ser CLIENT");

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository, times(1)).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();
        assertEquals(Roles.CLIENT, savedUser.getRole(), "La entidad enviada a la base de datos debe tener el rol CLIENT");
    }


    @Test
    @DisplayName("sessionUser return the jwt token")
    void sessionUserWithToken() {
        DTOCredentials credentials = new DTOCredentials("test@user.com", "password");
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        when(userEntityRepository.findByEmailUser("test@user.com")).thenReturn(Optional.of(userEntity));
        when(jwtService.generateToken(anyString(), any(), anyString())).thenReturn("mockedToken");

        DTOJwt result = userEntityService.sessionUser(credentials, responseMock);
        assertNotNull(result);
        assertEquals("test@user.com", result.getUseremail());
        assertEquals("mockedToken", result.getToken());

        verify(cookieService, times(1)).addHttpOnlyCookie(
                eq("jwt"),
                eq("mockedToken"),
                eq(7 * 24 * 60 * 60),
                eq(responseMock)
        );
    }

    @Test
    @DisplayName("updateUser")
    void updateUser() {
        //maybe i should do it



    }
}