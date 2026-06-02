package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.DTO.DTOReserved;
import com.MyMDentis.MyMDentistComerce.DTO.DTOReservedPetition;
import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import com.MyMDentis.MyMDentistComerce.Service.ReservedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MyMDentalCommerce/Reserved")
public class ReservedController {

    @Autowired
    private ReservedService reservedService;


    @GetMapping("/getAllReserved")
    public ResponseEntity<List<DTOReserved>> getAllReserved(){
        return ResponseEntity.ok(reservedService.getAllOrders());
    }

    @GetMapping("/getActiveReserved")
    public ResponseEntity<List<DTOReserved>> getActiveReserved(){
        return ResponseEntity.ok(reservedService.findActivesOrders());
    }

    @GetMapping("/getNoActiveReserved")
    public ResponseEntity<List<DTOReserved>> getNoActiveReserved(){
        return ResponseEntity.ok(reservedService.findNoActivesOrders());
    }

    @GetMapping("/getReservedById/{idReserved}")
    public ResponseEntity<DTOReserved> getReservedById(@PathVariable Long idReserved){
        return ResponseEntity.ok(reservedService.findOrderById(idReserved));
    }

    @GetMapping("/getReservedByUser/{idUserEntity}")
    public ResponseEntity<List<DTOReserved>> getAllReservedByUserEntity(@PathVariable Long idUserEntity){
        return ResponseEntity.ok(reservedService.findByUser(idUserEntity));

    }
    @PutMapping("/checkReserved/{idReserved}")
    public ResponseEntity<Boolean> disableReserved(@PathVariable Long idReserved){

        return ResponseEntity.ok(
                reservedService.checkReserved(idReserved)
        );
    }

    @PostMapping("/saveNewReserved")
    public ResponseEntity<List<DTOReserved>> saveNewReserved(@RequestBody List<DTOReservedPetition> petitions) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(reservedService.saveNewOrder(petitions));
    }

}
