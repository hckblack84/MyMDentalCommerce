package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTODepartment;
import com.MyMDentis.MyMDentistComerce.Exception.ExceptionValues;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Repository.DepartmentRepository;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    private final DTODepartment dtoDepartment = new DTODepartment();

    public List<DTODepartment> getAllDepartment(){

        List<Department> departments = departmentRepository.findAll();
        return dtoDepartment.parseToDTODepartmentList(departments);
    }

    public DTODepartment getDepartmentByName(String departmentName){
        Department department = departmentRepository.findByNameDepartment(departmentName).orElseThrow(() ->
                new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE));
        return dtoDepartment.parseToDTODepartment(department);
    }

    public DTODepartment getDepartmentById(Long idDepartment){
        Department department = departmentRepository.findById(idDepartment).orElseThrow(() ->
                new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE));
        return dtoDepartment.parseToDTODepartment(department);
    }
}
