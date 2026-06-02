package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOUserEntity;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(path = "/getUseremail/{email}")
    public ResponseEntity<DTOUserEntity> getByemail(@PathVariable String email)throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.findByemailUser(email));
    }
    @PutMapping(path = "/update/{email}")
    public ResponseEntity<DTOUserEntity> update(@PathVariable String email,@RequestBody DTOUserEntity dto) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.updateUser(email, dto, false));
    }
    @PutMapping(path = "/adminUpdate/{email}")
    public ResponseEntity<DTOUserEntity> adminUpdate(@PathVariable String email, @RequestBody DTOUserEntity dto) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(userEntityService.adminUpdateUser(email, dto, false));
    }

    @GetMapping(path="/findbyemail/{email}")
    public ResponseEntity<DTOUserEntity> userbyemail(@PathVariable String email)throws InterruptedException{
        Thread.sleep(1000L);
        return ResponseEntity.ok(userEntityService.findByemailUser(email));
    }

    @PutMapping("/updatePerfil/{email}")
    public ResponseEntity<?> updatePerfil(@PathVariable String email, @RequestBody DTOUserEntity dtoUserEntity) {

        try {
            DTOUserEntity usuarioActualizado = userEntityService.updateUserEntity(email, dtoUserEntity);
            return ResponseEntity.ok(usuarioActualizado);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}



