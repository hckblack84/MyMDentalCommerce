package com.MyMDentis.MyMDentistComerce.Verification;

import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductVerificationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductVerification productVerification;

    private DTOProductAdmin dtoProductAdmin;
    private Department mockedDepartment;

    @BeforeEach
    void setUp() {
        mockedDepartment = new Department(1L, "department");

        dtoProductAdmin = new DTOProductAdmin();
        dtoProductAdmin.setCodeProduct("123-456");
        dtoProductAdmin.setProductName("Cepillo Dental");
        dtoProductAdmin.setDescriptionProduct("Cepillo de cerdas suaves");
        dtoProductAdmin.setStockProduct(50L);
        dtoProductAdmin.setCriticProduct(10L);
        dtoProductAdmin.setPriceProduct(1500L);
        dtoProductAdmin.setCostPriceProduct(1000L);
        dtoProductAdmin.setNameDepartment(mockedDepartment.getNameDepartment());
    }

    //Null verifications tests

    @Test
    @DisplayName("Null Verification: Should return field name for null values")
    void nullVerification_shouldReturnFieldNameForNullValues() {
        dtoProductAdmin.setCodeProduct(null);
        assertEquals("Codigo del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setCodeProduct("");
        assertEquals("Codigo del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setCodeProduct("123-456");

        dtoProductAdmin.setProductName(null);
        assertEquals("Nombre del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setProductName("");
        assertEquals("Nombre del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setProductName("Cepillo Dental");

        dtoProductAdmin.setStockProduct(null);
        assertEquals("Stock del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setStockProduct(50L);

        dtoProductAdmin.setCriticProduct(null);
        assertEquals("Stock critico del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setCriticProduct(10L);

        dtoProductAdmin.setPriceProduct(null);
        assertEquals("Precio del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setPriceProduct(1500L);

        dtoProductAdmin.setCostPriceProduct(null);
        assertEquals("Costo de adquisicion del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setCostPriceProduct(1000L);

        dtoProductAdmin.setNameDepartment(null);
        assertEquals("Departamento del producto", productVerification.nullVerification(dtoProductAdmin));
        dtoProductAdmin.setNameDepartment(mockedDepartment.getNameDepartment());

        assertNull(productVerification.nullVerification(dtoProductAdmin));
    }

    //Invalid values tests

    @Test
    @DisplayName("Value Validation: Should return field name for invalid values")
    void validValues_shouldReturnFieldNameForInvalidValues() {
        dtoProductAdmin.setProductName("");
        assertEquals("Nombre del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setProductName("Cepillo Dental");

        dtoProductAdmin.setCodeProduct("");
        assertEquals("Codigo del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setCodeProduct("123-456");

        dtoProductAdmin.setDescriptionProduct("a".repeat(301));
        assertEquals("Descripcion del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setDescriptionProduct("Cepillo de cerdas suaves");

        dtoProductAdmin.setStockProduct(-1L);
        assertEquals("Stock del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setStockProduct(50L);

        dtoProductAdmin.setCriticProduct(-1L);
        assertEquals("Stock critico del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setCriticProduct(10L);

        dtoProductAdmin.setPriceProduct(-1L);
        assertEquals("Precio del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setPriceProduct(1500L);

        dtoProductAdmin.setCostPriceProduct(-1L);
        assertEquals("Costo de adquisicion del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setCostPriceProduct(1000L);

        dtoProductAdmin.setNameDepartment("");
        assertEquals("Departamento del producto", productVerification.validValues(dtoProductAdmin));
        dtoProductAdmin.setNameDepartment("Higiene");

        assertNull(productVerification.validValues(dtoProductAdmin));
    }

    //Logic tests

    @Test
    @DisplayName("Correlation: Price correlation should be true when price is less than cost")
    void validPriceCorrelation_shouldReturnTrueWhenPriceLessThanCost() {
        dtoProductAdmin.setPriceProduct(100L);
        dtoProductAdmin.setCostPriceProduct(200L);
        assertTrue(productVerification.validPriceCorrelation(dtoProductAdmin));
    }

    @Test
    @DisplayName("Correlation: Price correlation should be false when price is >= cost")
    void validPriceCorrelation_shouldReturnFalseWhenPriceGreaterThanOrEqualToCost() {
        dtoProductAdmin.setPriceProduct(200L);
        dtoProductAdmin.setCostPriceProduct(200L);
        assertFalse(productVerification.validPriceCorrelation(dtoProductAdmin));

        dtoProductAdmin.setPriceProduct(300L);
        assertFalse(productVerification.validPriceCorrelation(dtoProductAdmin));
    }

    @Test
    @DisplayName("Correlation: Stock correlation should be true when stock is <= critical")
    void validStockCorrelation_shouldReturnTrueWhenStockLessThanOrEqualToCritical() {
        dtoProductAdmin.setStockProduct(10L);
        dtoProductAdmin.setCriticProduct(10L);
        assertTrue(productVerification.validStockCorrelation(dtoProductAdmin));

        dtoProductAdmin.setStockProduct(5L);
        assertTrue(productVerification.validStockCorrelation(dtoProductAdmin));
    }

    @Test
    @DisplayName("Correlation: Stock correlation should be false when stock is > critical")
    void validStockCorrelation_shouldReturnFalseWhenStockGreaterThanCritical() {
        dtoProductAdmin.setStockProduct(20L);
        dtoProductAdmin.setCriticProduct(10L);
        assertFalse(productVerification.validStockCorrelation(dtoProductAdmin));
    }

    //multiples values testing
    @Test
    @DisplayName("Individual: Should validate product names correctly")
    void validProductName_shouldValidateNamesCorrectly() {
        assertTrue(productVerification.validProductName("Cepillo"));
        assertFalse(productVerification.validProductName(""));
        assertFalse(productVerification.validProductName("   "));
        assertFalse(productVerification.validProductName("a".repeat(101)));
    }

    @Test
    @DisplayName("Individual: Should validate product codes correctly")
    void validCodeProduct_shouldValidateCodesCorrectly() {
        assertTrue(productVerification.validCodeProduct("123"));
        assertFalse(productVerification.validCodeProduct(""));
        assertFalse(productVerification.validCodeProduct("   "));
        assertFalse(productVerification.validCodeProduct("a".repeat(30)));
    }
    
    @Test
    @DisplayName("Individual: Should validate descriptions correctly")
    void validDescriptionProduct_shouldValidateDescriptionsCorrectly() {
        assertTrue(productVerification.validDescriptionProduct("Description"));
        assertTrue(productVerification.validDescriptionProduct(null));
        assertTrue(productVerification.validDescriptionProduct(""));
        assertTrue(productVerification.validDescriptionProduct("   "));
        assertFalse(productVerification.validDescriptionProduct("a".repeat(300)));
    }

    @Test
    @DisplayName("Individual: Should validate stock correctly")
    void validStockProduct_shouldValidateStockCorrectly() {
        assertTrue(productVerification.validStockProduct(0L));
        assertTrue(productVerification.validStockProduct(500L));
        assertFalse(productVerification.validStockProduct(-1L));
        assertFalse(productVerification.validStockProduct(999999L));
    }

    @Test
    @DisplayName("Individual: Should validate critical stock correctly")
    void validCriticStockProduct_shouldValidateCriticalStockCorrectly() {
        assertTrue(productVerification.validCriticStockProduct(0L));
        assertTrue(productVerification.validCriticStockProduct(500L));
        assertFalse(productVerification.validCriticStockProduct(-1L));
        assertFalse(productVerification.validCriticStockProduct(999999L));
    }

    @Test
    @DisplayName("Individual: Should validate price correctly")
    void validPriceProduct_shouldValidatePriceCorrectly() {
        assertTrue(productVerification.validPriceProduct(0L));
        assertTrue(productVerification.validPriceProduct(1000L));
        assertFalse(productVerification.validPriceProduct(-1L));
        assertFalse(productVerification.validPriceProduct(9999999999L));
    }

    @Test
    @DisplayName("Individual: Should validate cost price correctly")
    void validCostPriceProduct_shouldValidateCostPriceCorrectly() {
        assertTrue(productVerification.validCostPriceProduct(0L));
        assertTrue(productVerification.validCostPriceProduct(1000L));
        assertFalse(productVerification.validCostPriceProduct(-1L));
        assertFalse(productVerification.validCostPriceProduct(9999999999L));
    }

    @Test
    @DisplayName("Individual: Should validate department names correctly")
    void validDepartmentName_shouldValidateDepartmentNamesCorrectly() {
        assertTrue(productVerification.validDepartmentName("Higiene"));
        assertFalse(productVerification.validDepartmentName(""));
        assertFalse(productVerification.validDepartmentName("   "));
        assertFalse(productVerification.validDepartmentName("a".repeat(30)));
    }

    @Test
    @DisplayName("Individual: Should validate code patterns correctly")
    void validPatter_shouldReturnTrueForValidPatterns() {
        assertTrue(productVerification.validPatter("123-456"));
        assertTrue(productVerification.validPatter("123:456"));
        assertTrue(productVerification.validPatter("123-456 789:012"));
        assertFalse(productVerification.validPatter("abc-def"));
    }
}
