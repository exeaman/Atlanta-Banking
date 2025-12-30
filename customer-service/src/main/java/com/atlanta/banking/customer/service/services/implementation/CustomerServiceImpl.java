package com.atlanta.banking.customer.service.services.implementation;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;
import com.atlanta.banking.customer.service.services.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    ModelMapper modelMapper;
    
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequest) {
        return null;
    }

    @Override
    public CustomerResponseDto getCustomerById(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerById'");
    }

    @Override
    public CustomerResponseDto getCustomerByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerByEmail'");
    }

    @Override
    public CustomerResponseDto updateCustomer(UUID customerId, CustomerRequestDto customerRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCustomer'");
    }

    @Override
    public void deactivateCustomer(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateCustomer'");
    }

    @Override
    public boolean customerExists(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'customerExists'");
    }

    @Override
    public CustomerResponseDto activateCustomer(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activateCustomer'");
    }

    @Override
    public void updateKycStatus(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateKycStatus'");
    }

}
