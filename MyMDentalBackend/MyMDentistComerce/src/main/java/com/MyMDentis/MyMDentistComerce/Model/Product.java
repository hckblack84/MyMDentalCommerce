package com.MyMDentis.MyMDentistComerce.Model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "Product")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    //Product Info

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", length = 8, unique = true, nullable = false)
    private Long idProduct;
    @Column(name = "code_product", length = 30, unique = false, nullable = false)
    private String codeProduct;
    @Column(name = "product_name", length = 100, unique = false, nullable = false)
    private String productName;
    @Column(name = "description_product", length = 300, unique = false, nullable = true)
    private String descriptionProduct;

    //Stock info

    @Column(name = "stock_product", length = 6, unique = false, nullable = false)
    private Long stockProduct;
    @Column(name = "critic_stock", length = 6, unique = false, nullable = false)
    private Long criticProduct;

    //Price info

    @Column(name = "price_product", length = 10, unique = false, nullable = false)
    private Long priceProduct;
    @Column(name = "cost_price_product", length = 10, unique = false, nullable = false)
    private Long costPriceProduct;

    //Foreign keys

    @ManyToOne(targetEntity = Department.class)
    private Department department;

}
