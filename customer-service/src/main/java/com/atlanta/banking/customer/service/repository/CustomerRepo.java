package com.atlanta.banking.customer.service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlanta.banking.customer.service.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer,UUID>{
    Customer getCustomerByEmail(String email);
    Customer getCustomerByPhoneNumber(String phoneNumber);
}
