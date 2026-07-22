package com.atlanta.banking.identity.service.repository;

import java.util.Optional;
import java.util.UUID;

import com.atlanta.banking.identity.service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmployeeId(String employeeId);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    boolean existsByUsername(String username);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}