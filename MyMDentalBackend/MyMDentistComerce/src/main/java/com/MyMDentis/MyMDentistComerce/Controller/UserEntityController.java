package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MyMDentalCommerce/users")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping(path = "/getUsers")
    public ResponseEntity<List<DTOUserEntity>> getAllUsers()throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.getAllUsers());
    }

    @PutMapping(path = "/update/{email}")
    @PreAuthorize("permitAll()" )
    public ResponseEntity<DTOUserEntity> update(@PathVariable String email,@RequestBody DTOUserEntity dto) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.updateUser(email, dto));
    }
    @PutMapping(path = "/adminUpdate/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOUserEntity> adminUpdate(@PathVariable String email, @RequestBody DTOUserEntity dto) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.adminUpdate(email, dto));
    }
}



