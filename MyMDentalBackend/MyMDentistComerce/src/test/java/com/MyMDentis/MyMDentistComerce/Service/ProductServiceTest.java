package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NullValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Repository.DepartmentRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Verification.ProductVerification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProductVerification productVerification;

    @Mock
    private S3Client s3Client;

    private ProductService productService;

    private DTOProductAdmin validDto;
    private MockMultipartFile validFile;
    private Department mockedDepartment;

    @BeforeEach
    void setUp() {

        productService = new ProductService(
                productRepository,
                productVerification,
                departmentRepository,
                "access key",
                "secret key",
                "session token"
        );

        ReflectionTestUtils.setField(productService, "s3Client", s3Client);

        ReflectionTestUtils.setField(productService, "bucketName", "mockedBucket");

        mockedDepartment = new Department();
        mockedDepartment.setIdDepartment(1L);
        mockedDepartment.setNameDepartment("mockedDepartment");

        validDto = new DTOProductAdmin();
        validDto.setCodeProduct("777777");
        validDto.setProductName("Mocked product");
        validDto.setStockProduct(10L);
        validDto.setCriticProduct(1L);
        validDto.setPriceProduct(2000L);
        validDto.setCostPriceProduct(1000L);
        validDto.setNameDepartment(mockedDepartment.getNameDepartment());

        validFile = new MockMultipartFile(
                "mockedFile",
                "mockedFile.jpg",
                "image/jpeg",
                "mockedFileContent".getBytes()
        );


    }

    @Test
    @DisplayName("saveNewProduct all validations pass")
    void saveNewProductAllValidationsPass() throws IOException {

        when(productVerification.nullVerification(any(DTOProductAdmin.class))).thenReturn(null); //Verifications true
        when(productVerification.validValues(any(DTOProductAdmin.class))).thenReturn(null); //Verifications true
        when(productVerification.validPatter(anyString())).thenReturn(false); //Pattern validate
        when(productVerification.validPriceCorrelation(any(DTOProductAdmin.class))).thenReturn(false); //price correlation true
        when(productVerification.validStockCorrelation(any(DTOProductAdmin.class))).thenReturn(false); //stock correlation true

        when(productRepository.findByCodeProduct(anyString())).thenReturn(Optional.empty()); //no return existing product by code
        when(productRepository.findByProductName(anyString())).thenReturn(Optional.empty()); //no return existing product by productName

        when(departmentRepository.findByNameDepartment("mockedDepartment")).thenReturn(Optional.of(mockedDepartment));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product productSaved = invocation.getArgument(0);
            productSaved.setIdProduct(1L);
            return productSaved;
        });

        DTOProductAdmin result = productService.saveNewProduct(validDto, validFile);

        assertNotNull(result);
        assertEquals("Mocked product", result.getProductName());

        verify(productVerification, times(1)).nullVerification(any(DTOProductAdmin.class));
        verify(productVerification, times(1)).validValues(any(DTOProductAdmin.class));
        verify(productRepository, times(1)).findByCodeProduct("777777");
        verify(productRepository, times(1)).findByProductName("Mocked product");
        verify(departmentRepository, times(2)).findByNameDepartment(mockedDepartment.getNameDepartment());
        verify(productRepository, times(2)).save(any(Product.class)); 
        verify(s3Client, times(1)).putObject(any(software.amazon.awssdk.services.s3.model.PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class));
    }

    @Test
    @DisplayName("Null exceptions productService")
    void throwNullExceptionsProductService() {
        when(productVerification.nullVerification(any(DTOProductAdmin.class))).thenReturn("Nombre del producto");

        assertThrows(NullValuesEntityException.class, () -> {
            productService.saveNewProduct(validDto, validFile);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Invalid values exceptions productService")
    void throwInvalidValuesEntityExceptionProductService() {

        when(productVerification.nullVerification(any(DTOProductAdmin.class))).thenReturn(null);
        when(productVerification.validValues(any(DTOProductAdmin.class))).thenReturn(null);
        when(productVerification.validPatter(anyString())).thenReturn(false);
        when(productVerification.validPriceCorrelation(any(DTOProductAdmin.class))).thenReturn(false);
        when(productVerification.validStockCorrelation(any(DTOProductAdmin.class))).thenReturn(false);

        when(productRepository.findByCodeProduct(anyString())).thenReturn(Optional.of(new Product())); //return a empty product

        assertThrows(InvalidValuesEntityException.class, () -> {
            productService.saveNewProduct(validDto, validFile);
        });

        verify(productRepository, never()).save(any(Product.class));
    }
}