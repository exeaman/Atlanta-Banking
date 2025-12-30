package com.atlanta.banking.customer.service.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotBlank(message = "This field cannot be blank.")
    private String firstName;

    @NotBlank(message = "This field cannot be blank.")
    private String lastName;

    @Email(message = "Provide a valid mail.")
    @Length(max = 254)
    private String email;

    @NotBlank(message = "This field cannot be blank.")
    @Size(min = 7, max = 20)
    private String phoneNumber;

    @NotNull(message = "This field cannot be blank.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "This field cannot be blank.")
    private String addressLine1;

    @NotNull(message = "This field cannot be blank.")
    private String addressLine2;

    @NotBlank(message = "This field cannot be blank.")
    private String city;

    @NotBlank(message = "This field cannot be blank.")
    private String state;

    @NotBlank(message = "This field cannot be blank.")
    private String postalCode;

    @NotBlank(message = "This field cannot be blank.")
    private String country;

}
