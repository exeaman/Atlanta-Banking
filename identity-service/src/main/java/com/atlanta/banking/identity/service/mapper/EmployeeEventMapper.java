package com.atlanta.banking.identity.service.mapper;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.event.EmployeeCreatedEvent;
import com.atlanta.banking.identity.service.event.EventMetadata;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EmployeeEventMapper {

    default EmployeeCreatedEvent toEmployeeCreatedEvent(Employee employee) {

        EventMetadata metadata = new EventMetadata(
                UUID.randomUUID(),
                null, // RequestContext later
                Instant.now(),
                "EMPLOYEE_CREATED",
                1
        );

        return new EmployeeCreatedEvent(
                metadata,
                employee.getSystemId(),
                employee.getEmployeeId(),
                employee.getUsername(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}