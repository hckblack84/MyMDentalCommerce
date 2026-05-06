package com.MyMDentis.MyMDentistComerce.Verification;

import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

@NoArgsConstructor
public class ProductVerification implements ProductAtributes{

    String patternCode = "^[0-9:\\-]+( [0-9:\\-]+)*$";
    @Autowired
    private ProductRepository productRepository;


    public boolean validPatter(String codeProduct){
        return Pattern.compile(patternCode).matcher(codeProduct).matches();
    }

    public String validValues(DTOProductAdmin dtoProductAdmin){
        if (!validProductName(dtoProductAdmin.getProductName())){
            return PRODUCT_NAME;
        }
        if (!validCodeProduct(dtoProductAdmin.getCodeProduct())){
            return PRODUCT_CODE;
        }
        if (!validDescriptionProduct(dtoProductAdmin.getDescriptionProduct())){
            return PRODUCT_DESCRIPTION;
        }
        if (!validStockProduct(dtoProductAdmin.getStockProduct())){
            return PRODUCT_STOCK;
        }
        if (!validCriticStockProduct(dtoProductAdmin.getCriticProduct())){
            return PRODUCT_CRITIC;
        }
        if (!validPriceProduct(dtoProductAdmin.getPriceProduct())){
            return PRODUCT_PRICE;
        }
        if (!validCostPriceProduct(dtoProductAdmin.getCostPriceProduct())){
            return PRODUCT_COST_PRICE;
        }
        if (!validDepartmentName(dtoProductAdmin.getNameDepartment())){
            return PRODUCT_DEPARTMENT;
        }
        return null;
    }

    public boolean validPriceCorrelation(DTOProductAdmin dtoProductAdmin){
        return dtoProductAdmin.getPriceProduct() >= dtoProductAdmin.getCostPriceProduct();
    }

    public boolean validStockCorrelation(DTOProductAdmin dtoProductAdmin){
        return dtoProductAdmin.getStockProduct() > dtoProductAdmin.getCriticProduct();
    }

    public boolean nullVerification(DTOProductAdmin dtoProductAdmin){
        if (nullProductCode(dtoProductAdmin.getCodeProduct())){
            return true;
        }
        if (nullProductName(dtoProductAdmin.getProductName())){
            return true;
        }
        if (nullProductStock(dtoProductAdmin.getStockProduct())){
            return true;
        }
        if (nullProductCritic(dtoProductAdmin.getCriticProduct())){
            return true;
        }
        if (nullProductPrice(dtoProductAdmin.getPriceProduct())){
            return true;
        }
        if (nullProductCostPrice(dtoProductAdmin.getCostPriceProduct())){
            return true;
        }
        if (nullProductNameDepartment(dtoProductAdmin.getNameDepartment())){
            return true;
        }
        return false;
    }

    //Basic validations


    public boolean validProductName(String productName){
        return productName.trim().length() <= 100 &&
                !productName.trim().isEmpty();
    }

    public boolean validCodeProduct(String codeProduct){
        return codeProduct.length() < 30 &&
                !codeProduct.trim().isEmpty();

    }

    public boolean validDescriptionProduct(String descriptionProduct){
        if(descriptionProduct != null && !descriptionProduct.trim().isEmpty()){
            return descriptionProduct.length() < 300;

        }
        return true;
    }

    public boolean validStockProduct(Long stockProduct){
        return stockProduct < 999999 && stockProduct >= 0;
    }

    public boolean validCriticStockProduct(Long criticStockProduct){
        return criticStockProduct < 999999 && criticStockProduct >= 0;
    }

    public boolean validPriceProduct(Long priceProduct){
        return priceProduct < 9999999999L && priceProduct >= 0;
    }

    public boolean validCostPriceProduct(Long costPriceProduct){
        return costPriceProduct < 9999999999L && costPriceProduct >= 0;
    }

    public boolean validDepartmentName(String departmentName){
        return departmentName.trim().length() < 30 && !departmentName.trim().isEmpty();
    }

    //Null validations

    public boolean nullProductName(String productName){
        return productName == null || productName.trim().isEmpty();
    }

    public boolean nullProductCode(String productCode){
        return productCode == null || productCode.trim().isEmpty();
    }

    public boolean nullProductStock(Long productStock){
        return productStock == null;
    }

    public boolean nullProductCritic(Long productCritic){
        return productCritic == null;
    }

    public boolean nullProductPrice(Long productPrice){
        return productPrice == null;
    }

    public boolean nullProductCostPrice(Long productCostPrice){
        return productCostPrice == null;
    }

    public boolean nullProductNameDepartment(String nameDepartment){
        return nameDepartment == null || nameDepartment.trim().isEmpty();
    }
}
