package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import com.atlanta.banking.identity.service.services.employee.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<CreateEmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(request));
    }

    @GetMapping("/{systemId}")
    public ResponseEntity<EmployeeResponse> getEmployeeBySystemId(
            @PathVariable UUID systemId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeBySystemId(systemId));
    }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmployeeId(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeByEmployeeId(employeeId));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findEmployees() {

        return ResponseEntity.ok(
                employeeService.findEmployees());
    }

    @PutMapping("/{systemId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable UUID systemId,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(systemId, request));
    }

    @PatchMapping("/{systemId}/enable")
    public ResponseEntity<EmployeeResponse> enableEmployee(
            @PathVariable UUID systemId) {

        return ResponseEntity.ok(
                employeeService.enableEmployee(systemId));
    }

    @PatchMapping("/{systemId}/disable")
    public ResponseEntity<EmployeeResponse> disableEmployee(
            @PathVariable UUID systemId) {

        return ResponseEntity.ok(
                employeeService.disableEmployee(systemId));
    }

    @PatchMapping("/{systemId}/terminate")
    public ResponseEntity<EmployeeResponse> terminateEmployee(
            @PathVariable UUID systemId) {

        return ResponseEntity.ok(
                employeeService.terminateEmployee(systemId));
    }
}