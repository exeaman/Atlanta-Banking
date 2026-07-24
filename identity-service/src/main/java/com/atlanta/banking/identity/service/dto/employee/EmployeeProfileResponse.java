package com.atlanta.banking.identity.service.dto.employee;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import lombok.Builder;

import java.util.Set;

@Builder
public record EmployeeProfileResponse(

        String employeeId, String username,

        String firstName, String lastName, String fullName,

        String email, String phoneNumber,

        Department department, Designation designation,

        Set<String> roles,

        boolean enabled

) {
}
