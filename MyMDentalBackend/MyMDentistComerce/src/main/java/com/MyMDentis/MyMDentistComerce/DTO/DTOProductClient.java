package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Product;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOProductClient {

    private Long idProduct;
    private String productName;
    private String descriptionProduct;
    private Long stockProduct;
    private Long criticProduct;
    private Long priceProduct;
    private String nameDepartment;

    public DTOProductClient parseDTOProductClient(Product product){
        return DTOProductClient.builder()
                .idProduct(product.getIdProduct())
                .productName(product.getProductName())
                .descriptionProduct(product.getDescriptionProduct())
                .stockProduct(product.getStockProduct())
                .criticProduct(product.getCriticProduct())
                .priceProduct(product.getPriceProduct())
                .nameDepartment(product.getDepartment().getNameDepartment())
                .build();
    }
}
