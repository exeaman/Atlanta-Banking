package com.atlanta.banking.identity.service.mapper;


import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "roles", ignore = true)
    Employee toEntity(CreateEmployeeRequest request);

    @Mapping(target = "roles", source = "roles")
    EmployeeResponse toResponse(Employee employee);

    List<EmployeeResponse> toResponseList(List<Employee> employees);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }

        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}