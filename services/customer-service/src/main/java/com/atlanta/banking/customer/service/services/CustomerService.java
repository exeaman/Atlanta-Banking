package com.atlanta.banking.customer.service.services;

import java.util.List;
import java.util.UUID;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;
import com.atlanta.banking.customer.service.utils.KycStatus;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequest);
    CustomerResponseDto getCustomerById(UUID customerId);
    CustomerResponseDto getCustomerByEmail(String email);
    CustomerResponseDto getCustomerByPhoneNumber(String phoneNumber);
    CustomerResponseDto updateCustomer(UUID customerId, CustomerRequestDto customerRequest);
    String deactivateCustomer(UUID customerId);
    boolean customerExists(UUID customerId);
    String activateCustomer(UUID customerId);
    String updateKycStatus(UUID customerId, KycStatus kycStatus);

    List<CustomerResponseDto> getAllCustomers();
}
