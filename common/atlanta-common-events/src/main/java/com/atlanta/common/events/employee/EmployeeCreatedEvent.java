package com.atlanta.common.events.employee;

import com.atlanta.common.events.metadata.EventMetadata;

import java.util.UUID;

public record EmployeeCreatedEvent(

        EventMetadata metadata,

        UUID systemId,

        String employeeId,

        String username,

        String firstName,

        String lastName,

        String email,

        String department

) {
}