package com.atlanta.banking.accounts.service.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.atlanta.banking.accounts.service.utils.AccountStatus;
import com.atlanta.banking.accounts.service.utils.AccountType;

import lombok.Data;

@Data
public class AccountResponseDto {
    private String userId;

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal balance;

    private AccountStatus accountStatus;

    private String currency;

    private Instant createdAt;
}
