package com.atlanta.banking.customer.service.services;

import java.util.UUID;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequest);
    CustomerResponseDto getCustomerById(UUID customerId);
    CustomerResponseDto getCustomerByEmail(String email);
    CustomerResponseDto updateCustomer(UUID customerId, CustomerRequestDto customerRequest);
    void deactivateCustomer(UUID customerId);
    boolean customerExists(UUID customerId);
    CustomerResponseDto activateCustomer(UUID customerId);
    void updateKycStatus(UUID customerId);
}
