package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import com.atlanta.banking.identity.service.services.employee.EmployeeService;
import com.atlanta.banking.identity.service.utils.documentation.EmployeeControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeControllerDocs {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
    }

    @GetMapping("/{systemId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SECURITY_ADMIN')")
    public ResponseEntity<EmployeeResponse> getEmployeeBySystemId(@PathVariable UUID systemId) {

        return ResponseEntity.ok(employeeService.getEmployeeBySystemId(systemId));
    }

    @GetMapping("/employee-id/{employeeId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SECURITY_ADMIN')")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmployeeId(@PathVariable String employeeId) {

        return ResponseEntity.ok(employeeService.getEmployeeByEmployeeId(employeeId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SECURITY_ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> findEmployees() {

        return ResponseEntity.ok(employeeService.findEmployees());
    }

    @PutMapping("/{systemId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable UUID systemId, @Valid @RequestBody UpdateEmployeeRequest request) {

        return ResponseEntity.ok(employeeService.updateEmployee(systemId, request));
    }

    @PatchMapping("/{systemId}/enable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<EmployeeResponse> enableEmployee(@PathVariable UUID systemId) {

        return ResponseEntity.ok(employeeService.enableEmployee(systemId));
    }

    @PatchMapping("/{systemId}/disable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<EmployeeResponse> disableEmployee(@PathVariable UUID systemId) {

        return ResponseEntity.ok(employeeService.disableEmployee(systemId));
    }

    @PatchMapping("/{systemId}/terminate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<EmployeeResponse> terminateEmployee(@PathVariable UUID systemId) {

        return ResponseEntity.ok(employeeService.terminateEmployee(systemId));
    }
}