package com.MyMDentis.MyMDentistComerce.Model;

import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Table(name = "reserved_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Reserved {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserved", unique = true, nullable = false, length = 8)
    private Long idReserved;
    @Column(name = "code_reserved", unique = true, nullable = false, length = 100)
    private String codeReserved;
    @Column(name = "quantity_reserved", unique = false, nullable = false, length = 8)
    private Long quantityReserved;

    //TIME
    @Column(name = "start_date_reserved", unique = false, nullable = false, length = 20)
    private Date startDate;
    @Column(name = "expiration_date_reserved", unique = false, nullable = false, length = 20)
    private Date expirationDate;

    @ManyToOne
    private Product product;

    @ManyToOne
    private UserEntity userEntity;

    @Column
    private boolean activeReserved;


}
