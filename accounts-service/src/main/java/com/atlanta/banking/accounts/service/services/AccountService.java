package com.atlanta.banking.accounts.service.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.atlanta.banking.accounts.service.dto.AccountCreationRequestDto;
import com.atlanta.banking.accounts.service.dto.AccountResponseDto;

public interface AccountService {
    AccountResponseDto openAccount(AccountCreationRequestDto accountCreationRequestDto );

    AccountResponseDto getAccountByAccountNumber(String accountNumber);

    List<AccountResponseDto> getAccountByCustomerId(String customerId);

    void freezeAccount(String accountNumber);

    void unFreezeAccount(String accountNumber);

    void closeAccount(String accountNumber);

    void credit(String accountNumber, BigDecimal amount);

    void debit(String accountNumber, BigDecimal amount);

    Boolean isAccountActive(String accountNumber);

    BigDecimal getBalance(String accountNumber);

    Boolean validateCustomer(String customerId);

    Boolean hasAccount(UUID customerId);

    List<AccountResponseDto> getAllAccounts();
}
