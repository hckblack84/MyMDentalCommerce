package com.MyMDentis.MyMDentistComerce.DTO;

import com.MyMDentis.MyMDentistComerce.Model.Department;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTODepartment {

    public Long idDepartment;
    private String nameDepartment;

    public DTODepartment parseToDTODepartment(Department department){
        return new DTODepartment(department.getIdDepartment(), department.getNameDepartment());
    }

    public List<DTODepartment> parseToDTODepartmentList(List<Department> departments) {
        List<DTODepartment> dtoDepartments = new ArrayList<>();
        for (Department department : departments){
            dtoDepartments.add(parseToDTODepartment(department));
        }
        return dtoDepartments;

    }
}
