package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTODepartment;
import com.MyMDentis.MyMDentistComerce.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MyMDentalCommerce/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @GetMapping(path = "/getDepartments")
    public List<DTODepartment> getAllDepartments() throws InterruptedException {
        Thread.sleep(2000L);
        return departmentService.getAllDepartment();
    }

    @PostMapping(path = "/createDepartment")
    public DTODepartment createDepartment(@RequestBody DTODepartment dtoDepartment) throws InterruptedException {
        Thread.sleep(2000L);
        return departmentService.createDepartment(dtoDepartment);
    }

}
