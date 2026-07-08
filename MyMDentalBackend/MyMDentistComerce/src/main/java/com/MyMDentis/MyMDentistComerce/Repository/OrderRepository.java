package com.MyMDentis.MyMDentistComerce.Repository;

import com.MyMDentis.MyMDentistComerce.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
