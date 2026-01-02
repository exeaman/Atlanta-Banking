package com.atlanta.banking.accounts.service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlanta.banking.accounts.service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID>{
    Account findByUserId(String userId);
    Account findByAccountNumber(String accountNumber);
}
