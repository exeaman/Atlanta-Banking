package com.atlanta.banking.accounts.service.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlanta.banking.accounts.service.dto.AccountCreationRequestDto;
import com.atlanta.banking.accounts.service.dto.AccountResponseDto;
import com.atlanta.banking.accounts.service.services.implementation.AccountServiceImplementation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountServiceImplementation accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(
            @RequestBody AccountCreationRequestDto accountCreationRequestDto) {
        return new ResponseEntity<>(accountService.openAccount(accountCreationRequestDto), HttpStatus.OK);
    }

    @GetMapping("/byaccnumber/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.getAccountByAccountNumber(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/bycustomerid/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountById(@PathVariable String customerId) {
        return new ResponseEntity<>(accountService.getAccountByCustomerId(customerId), HttpStatus.OK);

    }

    @PutMapping("/freeze/{accountNumber}")
    public ResponseEntity<Void> freezeAccount(@PathVariable String accountNumber) {
        accountService.freezeAccount(accountNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/close/{accountNumber}")
    public ResponseEntity<Void> closeAccount(@PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/credit/{accountNumber}")
    public ResponseEntity<Void> credit(@PathVariable String accountNumber, @RequestBody BigDecimal amount) {
        accountService.credit(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/debit/{accountNumber}")
    public ResponseEntity<Void> debit(@PathVariable String accountNumber, @RequestBody BigDecimal amount) {
        accountService.debit(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfreeze/{accountNumber}")
    public ResponseEntity<Void> unFreezeAccount(@PathVariable String accountNumber) {
        accountService.unFreezeAccount(accountNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isactive/{accountNumber}")
    public ResponseEntity<Boolean> isAccountActive(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.isAccountActive(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/by-customer-id/{customerId}")
    public ResponseEntity<Boolean> validateCustomer(@PathVariable String customerId) {
        return new ResponseEntity<>(accountService.validateCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        return new ResponseEntity<>(accountService.getBalance(accountNumber), HttpStatus.OK);
    }
}
