package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReserved;
import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import com.MyMDentis.MyMDentistComerce.Service.ReservedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MyMDentalCommerce/Orders")
public class ReservedController {

    @Autowired
    private ReservedService reservedService;


    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Reserved>> getAllOrders(){
        return ResponseEntity.ok(reservedService.getAllOrders());
    }

    @GetMapping("/getActiveOrders")
    public ResponseEntity<List<Reserved>> getActiveOrders(){
        return ResponseEntity.ok(reservedService.findActivesOrders());
    }

    @GetMapping("/getNoActiveOrders")
    public ResponseEntity<List<Reserved>> getNoActiveOrders(){
        return ResponseEntity.ok(reservedService.findNoActivesOrders());
    }

    @GetMapping("/getOrderById/{idOrder}")
    public ResponseEntity<Reserved> getOrderById(@PathVariable Long idOrder){
        return ResponseEntity.ok(reservedService.findOrderById(idOrder));
    }

    @GetMapping("/getOrdersByUser/{idUserEntity}")
    public ResponseEntity<List<Reserved>> getAllOrdersByUserEntity(@PathVariable Long idUserEntity){
        return ResponseEntity.ok(reservedService.findByUser(idUserEntity));
    }

    @PostMapping("/saveNewReserved")
    @PreAuthorize("hasRole(T(com.MyMDentis.MyMDentistComerce.Model.Roles).ADMINISTRATOR.name())")
    public ResponseEntity<List<DTOReserved>> saveNewReserved(@RequestBody List<DTOReservedPetition> petitions){
        return ResponseEntity.ok(reservedService.saveNewOrder(petitions));
    }

}
