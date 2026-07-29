package com.atlanta.banking.accounts.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.atlanta.banking.accounts.service.utils.AccountStatus;
import com.atlanta.banking.accounts.service.utils.AccountType;

import lombok.Data;

@Data
public class AccountDto {
    private UUID accountId;

    private String userId;
    private String accountNumber;

    private AccountType accountType;
    private BigDecimal balance;

    private AccountStatus accountStatus;

    private String currency;

    private Boolean isActive = true;

    private Instant createdAt;

    private Instant updatedAt;

}
