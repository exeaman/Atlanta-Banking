package com.atlanta.banking.customer.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.atlanta.banking.customer.service.utils.KycStatus;

import lombok.Data;

@Data
public class CustomerDto {
    private UUID customerId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private KycStatus kycStatus;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
