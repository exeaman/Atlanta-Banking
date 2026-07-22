package com.atlanta.banking.identity.service.dto.employee;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private UUID systemId;

    private String employeeId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Department department;

    private Designation designation;

    private Boolean enabled;

    private Boolean employed;

    private Boolean accountLocked;

    private Boolean accountExpired;

    private Boolean credentialsExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    private Set<String> roles;
}