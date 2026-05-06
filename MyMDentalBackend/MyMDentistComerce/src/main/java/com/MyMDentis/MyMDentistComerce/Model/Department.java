package com.MyMDentis.MyMDentistComerce.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department", nullable = false, length = 3, unique = true)
    private Long idDepartment;
    @Column(name = "name_department", unique = true, length = 30, nullable = false)
    private String nameDepartment;


}
