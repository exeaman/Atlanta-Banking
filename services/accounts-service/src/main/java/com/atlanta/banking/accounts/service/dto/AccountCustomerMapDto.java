package com.atlanta.banking.accounts.service.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCustomerMapDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID customerId;
    @NotNull
    private String accountNumber;
    @NotNull
    private String userId;
    @NotNull
    private Instant createdAt;
}
