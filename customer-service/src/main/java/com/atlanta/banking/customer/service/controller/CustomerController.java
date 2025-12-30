package com.atlanta.banking.customer.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @GetMapping("/hi")
    public ResponseEntity<String> hiCustomer(){
        return new ResponseEntity<>("Hello Customer", HttpStatus.OK);
    }
}