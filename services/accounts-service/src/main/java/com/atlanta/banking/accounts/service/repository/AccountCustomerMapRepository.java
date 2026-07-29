package com.atlanta.banking.accounts.service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlanta.banking.accounts.service.entity.AccountCustomerMap;

public interface AccountCustomerMapRepository extends JpaRepository<AccountCustomerMap,UUID>{
    Optional<AccountCustomerMap> findByUserId(String userId);
    Optional<AccountCustomerMap> findByAccountNumber(String accountNumber);
    List<AccountCustomerMap> findAllByCustomerId(UUID customerId);
}
