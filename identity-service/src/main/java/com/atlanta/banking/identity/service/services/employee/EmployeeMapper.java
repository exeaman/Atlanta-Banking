package com.atlanta.banking.identity.service.services.employee;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.entity.Employee;

public interface EmployeeMapper {

    Employee toEntity(CreateEmployeeRequest request);

    EmployeeResponse toResponse(Employee employee);
}