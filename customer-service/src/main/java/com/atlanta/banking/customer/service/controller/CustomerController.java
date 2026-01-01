package com.atlanta.banking.customer.service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;
import com.atlanta.banking.customer.service.services.implementation.CustomerServiceImpl;
import com.atlanta.banking.customer.service.utils.KycStatus;

import ch.qos.logback.core.model.processor.PhaseIndicator;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;

    @GetMapping("/id/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable String customerId) {
        return new ResponseEntity<>(customerServiceImpl.getCustomerById(UUID.fromString(customerId)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return new ResponseEntity<>(customerServiceImpl.getAllCustomers(), HttpStatus.OK);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable String customerId,
            @RequestBody CustomerRequestDto customerRequestDto) {
        return new ResponseEntity<>(customerServiceImpl.updateCustomer(UUID.fromString(customerId), customerRequestDto),
                HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        return new ResponseEntity<>(customerServiceImpl.createCustomer(customerRequestDto), HttpStatus.OK);
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<CustomerResponseDto> getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(customerServiceImpl.getCustomerByPhoneNumber(phoneNumber), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
        return new ResponseEntity<>(customerServiceImpl.getCustomerByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/update-kyc")
    public ResponseEntity<String> updateKyc(@RequestBody String customerId, @RequestBody KycStatus kycStatus) {
        return new ResponseEntity<>(customerServiceImpl.updateKycStatus(UUID.fromString(customerId), kycStatus),
                HttpStatus.OK);
    }

    @PutMapping("/activate/{customerId}")
    public ResponseEntity<String> activateCustomer(@PathVariable String customerId) {
        return new ResponseEntity<>(customerServiceImpl.activateCustomer(UUID.fromString(customerId)), HttpStatus.OK);
    }

    @PutMapping("/deactivate/{customerId}")
    public ResponseEntity<String> deactivateCustomer(@PathVariable String customerId) {
        return new ResponseEntity<>(customerServiceImpl.deactivateCustomer(UUID.fromString(customerId)), HttpStatus.OK);
    }

    @GetMapping("/check-customer/{customerId}")
    public ResponseEntity<Boolean> customerExists(@PathVariable String customerId) {
        return new ResponseEntity<>(customerServiceImpl.customerExists(UUID.fromString(customerId)), HttpStatus.OK);
    }
}