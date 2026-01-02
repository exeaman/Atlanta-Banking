package com.atlanta.banking.accounts.service.services.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.atlanta.banking.accounts.service.dto.AccountCreationRequestDto;
import com.atlanta.banking.accounts.service.dto.AccountResponseDto;
import com.atlanta.banking.accounts.service.services.AccountService;

public class AccountServiceImplementation implements AccountService{

    @Override
    public AccountResponseDto openAccount(AccountCreationRequestDto accountCreationRequestDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openAccount'");
    }

    @Override
    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountByAccountNumber'");
    }

    @Override
    public List<AccountResponseDto> getAccountByCustomerId(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountByCustomerId'");
    }

    @Override
    public void freezeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'freezeAccount'");
    }

    @Override
    public void unFreezeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unFreezeAccount'");
    }

    @Override
    public void closeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeAccount'");
    }

    @Override
    public void credit(String accountNumber, BigDecimal amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'credit'");
    }

    @Override
    public void debit(String accountNumber, BigDecimal amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debit'");
    }

    @Override
    public Boolean isAccountActive(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountActive'");
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

}
