package com.atlanta.banking.identity.service.mapper;

import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "expiresIn", source = "expiresIn")
    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "username", source = "employee.username")
    @Mapping(target = "mustChangePassword", source = "employee.credentialsExpired")
    @Mapping(
            target = "fullName",
            expression = "java(employee.getFirstName() + \" \" + employee.getLastName())"
    )
    @Mapping(
            target = "roles",
            expression = "java(employee.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()))"
    )
    @Mapping(target = "tokenType", constant = "Bearer")
    LoginResponse toLoginResponse(
            Employee employee,
            String accessToken,
            long expiresIn
    );
}