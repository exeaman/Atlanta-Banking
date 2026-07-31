package com.atlanta.banking.identity.service.services.employee.impl;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import com.atlanta.banking.identity.service.exception.common.DuplicateResourceException;
import com.atlanta.banking.identity.service.exception.common.ResourceNotFoundException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyDisabledException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyEnabledException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyTerminatedException;
import com.atlanta.banking.identity.service.exception.role.InvalidRoleAssignmentException;
import com.atlanta.banking.identity.service.kafka.EmployeeEventPublisher;
import com.atlanta.banking.identity.service.mapper.EmployeeMapper;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.repository.RoleRepository;
import com.atlanta.banking.identity.service.services.employee.EmployeeService;
import com.atlanta.banking.identity.service.utils.generator.EmployeeIdGenerator;
import com.atlanta.banking.identity.service.utils.generator.TemporaryPasswordGenerator;
import com.atlanta.banking.identity.service.utils.generator.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeIdGenerator employeeIdGenerator;
    private final UsernameGenerator usernameGenerator;
    private final TemporaryPasswordGenerator temporaryPasswordGenerator;
    private final EmployeeEventPublisher employeeEventPublisher;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (employeeRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        Employee employee = employeeMapper.toEntity(request);

        employee.setEmployeeId(employeeIdGenerator.generate());

        String username;

        do {
            username = usernameGenerator.generate(request.getFirstName(), request.getLastName());
        } while (employeeRepository.existsByUsername(username));

        employee.setUsername(username);

        String temporaryPassword = temporaryPasswordGenerator.generate();
        employee.setPassword(passwordEncoder.encode(temporaryPassword));

        List<Role> requestedRoles = roleRepository.findAllByNameIn(request.getRoles());

        if (requestedRoles.size() != request.getRoles().size()) {
            throw new InvalidRoleAssignmentException("One or more roles are invalid.");
        }

        employee.setRoles(new HashSet<>(requestedRoles));

        Employee savedEmployee = employeeRepository.save(employee);
        employeeEventPublisher.publishEmployeeCreated(savedEmployee);
        return employeeMapper.toCreateResponse(savedEmployee, temporaryPassword);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeResponse getEmployeeBySystemId(UUID systemId) {
        return employeeMapper.toResponse(getEmployee(systemId));
    }

    @Override
    public EmployeeResponse getEmployeeByEmployeeId(String employeeId) {
        return employeeMapper.toResponse(employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee with employee ID " + employeeId + " not found.")));

    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeResponse> findEmployees() {
        return employeeMapper.toResponseList(employeeRepository.findAll());
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployee(UUID systemId, UpdateEmployeeRequest request) {

        Employee employee = getEmployee(systemId);
        if (!employee.getEmail().equals(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (!employee.getPhoneNumber().equals(request.getPhoneNumber()) && employeeRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeResponse enableEmployee(UUID systemId) {
        Employee employee = getEmployee(systemId);
        if (!employee.getEmployed()) throw new EmployeeAlreadyTerminatedException("This employee has been terminated!");
        if (employee.getEnabled()) throw new EmployeeAlreadyEnabledException("This employee is already enabled!");
        employee.setEnabled(Boolean.TRUE);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeResponse disableEmployee(UUID systemId) {
        Employee employee = getEmployee(systemId);

        if (!employee.getEmployed()) {
            throw new EmployeeAlreadyTerminatedException("This employee has been terminated!");
        }

        if (!employee.getEnabled()) {
            throw new EmployeeAlreadyDisabledException("This employee is already disabled!");
        }

        employee.setEnabled(Boolean.FALSE);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    @Override
    public EmployeeResponse terminateEmployee(UUID systemId) {
        Employee employee = getEmployee(systemId);

        if (!employee.getEmployed()) {
            throw new EmployeeAlreadyTerminatedException("This employee has already been terminated!");
        }

        employee.setEmployed(Boolean.FALSE);
        employee.setEnabled(Boolean.FALSE);

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    private Employee getEmployee(UUID systemId) {
        return employeeRepository.findById(systemId).orElseThrow(() -> new ResourceNotFoundException("Employee with system ID " + systemId + " not found."));
    }
}