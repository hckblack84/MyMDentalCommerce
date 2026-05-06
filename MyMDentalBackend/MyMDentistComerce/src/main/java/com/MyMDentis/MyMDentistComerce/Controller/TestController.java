package com.MyMDentis.MyMDentistComerce.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping()
    public String hello(@RequestParam(name = "product") String product, @RequestParam(name = "filter") String filter){
        return "you want access to " + product + " with filter " + filter;
    }


}
