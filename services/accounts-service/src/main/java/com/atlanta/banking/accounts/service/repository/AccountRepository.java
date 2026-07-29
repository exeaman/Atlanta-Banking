package com.atlanta.banking.accounts.service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlanta.banking.accounts.service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID>{
    Optional<Account> findByUserId(String userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
