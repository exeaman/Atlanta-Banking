package com.atlanta.banking.accounts.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlanta.banking.accounts.service.services.implementation.AccountServiceImplementation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountTestController {

    private final AccountServiceImplementation accService;
    @GetMapping("/by-customer-id/{customerId}")
    public ResponseEntity<Boolean> validateCustomer(@PathVariable String customerId){
        return new ResponseEntity<>(accService.validateCustomer(customerId), HttpStatus.OK);
    }
}
