package com.atlanta.banking.audit.service.event;

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