package com.atlanta.banking.accounts.service.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.atlanta.banking.accounts.service.utils.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AccountCreationRequestDto {
    @NotNull(message = "Customer ID cannot be null")
    private UUID customerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType; 

    @Length(min = 3, message = "Currency code must be at least 3 characters")
    private String currency;
}

