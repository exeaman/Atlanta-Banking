package com.atlanta.banking.customer.service.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/customer")
public class CustomerTestController {
@GetMapping("/health")
    public ResponseEntity<Void> getCustomerHealth(){
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
