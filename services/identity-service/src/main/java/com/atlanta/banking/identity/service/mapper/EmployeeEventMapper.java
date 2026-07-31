package com.atlanta.banking.identity.service.mapper;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.common.events.employee.EmployeeCreatedEvent;
import com.atlanta.common.events.metadata.EventMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeEventMapper {

    @Mapping(target = "metadata", source = "metadata")
    @Mapping(target = "department", expression = "java(employee.getDepartment().name())")
    EmployeeCreatedEvent toEmployeeCreatedEvent(
            Employee employee,
            EventMetadata metadata
    );
}