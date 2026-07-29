package com.atlanta.banking.identity.service.event;

import com.atlanta.banking.identity.service.enums.Department;

import java.util.UUID;

public record EmployeeCreatedEvent(
        EventMetadata metadata,

        UUID systemId,
        String employeeId,
        String username,
        String firstName,
        String lastName,
        String email,
        Department department
) {
}