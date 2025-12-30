package com.atlanta.banking.customer.service.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.atlanta.banking.customer.service.utils.KycStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID customerId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, length = 254)
    private String email;

    @NotBlank
    @Size(min = 7, max = 20)
    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @Enumerated(EnumType.STRING)
    @NotNull
    private KycStatus kycStatus;

    @NotNull
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    
@PrePersist
protected void onCreate(){
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
    if (this.isActive == null) this.isActive = true;
    if (this.kycStatus == null) this.kycStatus = KycStatus.PENDING;
}


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
