package com.atlanta.banking.identity.service.dto.employee;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    @NotBlank(message = "First name is required.")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "First name may contain only letters, spaces, hyphens (-), and apostrophes (').")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "Last name may contain only letters, spaces, hyphens (-), and apostrophes (').")
    private String lastName;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    @Size(max = 254, message = "Email address must not exceed 254 characters.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number.")
    private String phoneNumber;

    @NotNull(message = "Department is required.")
    private Department department;

    @NotNull(message = "Designation is required.")
    private Designation designation;

    @NotEmpty(message = "At least one role must be assigned.")
    @Size(min = 1, message = "At least one role must be be assigned.")
    private Set<String> roles;
}