package com.MyMDentis.MyMDentistComerce.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Order {

    //UUID class
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Reserved> reservedItems = new ArrayList<>();

    @Column(name = "mp_preference_id")
    private String mercadoPagoPreferenceId;

    @Column(name = "mp_payment_id")
    private String mercadoPagoPaymentId;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    public void addReservedItem(Reserved reserved) {
        this.reservedItems.add(reserved);
        reserved.setOrder(this);
    }
}