package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Product;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOProductAdmin {

    private String codeProduct;
    private String productName;
    private String descriptionProduct;
    private Long stockProduct;
    private Long criticProduct;
    private Long priceProduct;
    private Long costPriceProduct;
    private String nameDepartment;
    private String urlProduct;
    private boolean activeProduct;



    public DTOProductAdmin parseDTOProductAdmin(Product product){
        return DTOProductAdmin.builder()
                .codeProduct(product.getCodeProduct())
                .productName(product.getProductName())
                .descriptionProduct(product.getDescriptionProduct())
                .stockProduct(product.getStockProduct())
                .criticProduct(product.getCriticProduct())
                .priceProduct(product.getPriceProduct())
                .costPriceProduct(product.getCostPriceProduct())
                .nameDepartment(product.getDepartment().getNameDepartment())
                .urlProduct(product.getUrlProduct())
                .activeProduct(product.isActiveProduct())
                .build();
    }

}
