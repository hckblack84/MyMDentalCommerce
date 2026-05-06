package com.MyMDentis.MyMDentistComerce.Repository;

import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCodeProduct(String codeProduct);
    Optional<Product> findByProductName(String productName);
    void deleteByProductName(String productName);
    List<Product> findByDepartment(Department department);
    long countByDepartment(Department department);
    Page<Product> findByDepartment(Department department, Pageable pageable);

}
