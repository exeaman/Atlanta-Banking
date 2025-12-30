package com.atlanta.banking.customer.service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atlanta.banking.customer.service.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,UUID>{
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
