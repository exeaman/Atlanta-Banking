package com.atlanta.banking.identity.service.services.employee;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;

import java.util.UUID;
import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeBySystemId(UUID systemId);

    EmployeeResponse getEmployeeByEmployeeId(String employeeId);

    List<EmployeeResponse> findEmployees();

    EmployeeResponse updateEmployee(
            UUID systemId,
            UpdateEmployeeRequest request
    );

    EmployeeResponse enableEmployee(UUID systemId);

    EmployeeResponse disableEmployee(UUID systemId);

    EmployeeResponse terminateEmployee(UUID systemId);
}