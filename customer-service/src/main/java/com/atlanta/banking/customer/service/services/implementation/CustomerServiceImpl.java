package com.atlanta.banking.customer.service.services.implementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;
import com.atlanta.banking.customer.service.entity.Customer;
import com.atlanta.banking.customer.service.exception.CustomerNotFoundException;
import com.atlanta.banking.customer.service.exception.InvalidCustomerStateException;
import com.atlanta.banking.customer.service.repository.CustomerRepo;
import com.atlanta.banking.customer.service.services.CustomerService;
import com.atlanta.banking.customer.service.utils.KycStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequest) {
        return modelMapper
                .map(customerRepo
                        .save(modelMapper
                                .map(customerRequest, Customer.class)),
                        CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto getCustomerById(UUID customerId) {
        return modelMapper
                .map(customerRepo.findById(customerId)
                        .orElseThrow(() -> new CustomerNotFoundException("No customer with such ID exists.")),
                        CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto getCustomerByEmail(String email) {
        return modelMapper
                .map(customerRepo.findByEmail(email)
                        .orElseThrow(() -> new CustomerNotFoundException("No customer with such email exists.")),
                        CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto updateCustomer(UUID customerId, CustomerRequestDto customerRequest) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("No customer with such ID exists."));
        modelMapper.map(customerRequest, customer);
        return modelMapper.map(customerRepo.save(customer), CustomerResponseDto.class);
    }

    @Override
    public String deactivateCustomer(UUID customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("No customer with such ID exists."));

        customer.setIsActive(false);

        customerRepo.save(customer);

        return "Customer deactivated successfully.";
    }

    @Override
    public boolean customerExists(UUID customerId) {
        return customerRepo.existsById(customerId);
    }

    @Override
    public String activateCustomer(UUID customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("No customer with such ID exists."));

        if (customer.getIsActive())
            throw new InvalidCustomerStateException("Customer is already active.");

        customer.setIsActive(true);

        customerRepo.save(customer);

        return "Customer activated successfully.";
    }

    @Override
    public String updateKycStatus(UUID customerId, KycStatus kycStatus) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("No customer with such ID exists."));

        if (customer.getKycStatus() == kycStatus)
            throw new InvalidCustomerStateException("Customer's KYC is already " + kycStatus.toString());

        customer.setKycStatus(kycStatus);

        customerRepo.save(customer);
        return "KYC updated successfully.";
    }

    @Override
    public CustomerResponseDto getCustomerByPhoneNumber(String phoneNumber) {
        return modelMapper
                .map(customerRepo.findByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new CustomerNotFoundException("No customer with such phone number exists.")),
                        CustomerResponseDto.class);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        if (customers.isEmpty())
            throw new InvalidCustomerStateException("No customer data present.");
        return customers.stream()
                .map(c -> modelMapper.map(c, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }
}