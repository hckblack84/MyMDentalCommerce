package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ReservedRepository;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservedServiceTest {

    @Mock
    private ReservedRepository reservedRepository;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReservedService reservedService;

    private UserEntity user;
    private Product product;
    private DTOReservedPetition petition;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setIdUser(1L);
        user.setEmailUser("test@user.com");

        product = new Product();
        product.setIdProduct(1L);
        product.setStockProduct(100L);
        product.setCodeProduct("777777");

        petition = new DTOReservedPetition();
        petition.setIdProduct(1L);
        petition.setQuantityReserved(5L);
    }

    @Test
    @DisplayName("saveNewOrder should create reserved when stock is sufficient")
    void saveNewOrder_shouldCreateReserved_whenStockIsSufficient() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserDetails userDetails = mock(UserDetails.class);
        try (MockedStatic<SecurityContextHolder> mockedContext = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            when(authentication.getPrincipal())
                    .thenReturn(userDetails);
            when(userDetails.getUsername())
                    .thenReturn("test@user.com");

            when(userEntityRepository.findByEmailUser("test@user.com"))
                    .thenReturn(Optional.of(user));
            when(productRepository.findById(1L))
                    .thenReturn(Optional.of(product));
            when(reservedRepository.save(any(Reserved.class)))
                    .thenAnswer(inv -> inv.getArgument(0));
            reservedService.saveNewOrder(List.of(petition));

            assertEquals(95L, product.getStockProduct());
            verify(productRepository, times(1)).save(product);
            verify(reservedRepository, times(1)).save(any(Reserved.class));
        }
    }

    @Test
    @DisplayName("saveNewOrder should throw InvalidValuesEntityException when stock is insufficient")
    void saveNewOrder_shouldThrowException_whenStockIsInsufficient() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserDetails userDetails = mock(UserDetails.class);

        try (MockedStatic<SecurityContextHolder> mockedContext = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            when(authentication.getPrincipal())
                    .thenReturn(userDetails);
            when(userDetails.getUsername())
                    .thenReturn("test@user.com");
            product.setStockProduct(3L);
            when(userEntityRepository.findByEmailUser("test@user.com"))
                    .thenReturn(Optional.of(user));
            when(productRepository.findById(1L))
                    .thenReturn(Optional.of(product));
            assertThrows(InvalidValuesEntityException.class, () -> {
                reservedService.saveNewOrder(List.of(petition));
            });
            verify(reservedRepository, never()).save(any(Reserved.class));
        }
    }

    @Test
    @DisplayName("findOrderById throw exception for negative ID")
    void findOrderByIdExceptionNegativeId() {
        assertThrows(InvalidValuesEntityException.class, () -> {
            reservedService.findOrderById(-1L);
        });

    }

    @Test
    @DisplayName("checkReserved should set reserved to inactive")
    void checkReserved_shouldSetToInactive() {
        Reserved activeReserved = new Reserved();
        activeReserved.setIdReserved(1L);
        activeReserved.setActiveReserved(true);
        when(reservedRepository.findById(1L)).thenReturn(Optional.of(activeReserved));

        boolean result = reservedService.checkReserved(1L);
        assertTrue(result);
        ArgumentCaptor<Reserved> reservedCaptor = ArgumentCaptor.forClass(Reserved.class);
        verify(reservedRepository, times(1)).save(reservedCaptor.capture());
        Reserved savedReserved = reservedCaptor.getValue();
        assertFalse(savedReserved.isActiveReserved());
    }

    @Test
    @DisplayName("findByUser should throw exception if user not found")
    void findByUser_shouldThrowException_ifUserNotFound() {
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> {
            reservedService.findByUser(67L);
        });
        verify(reservedRepository, never()).findByUserEntity(any(UserEntity.class));
    }
}
