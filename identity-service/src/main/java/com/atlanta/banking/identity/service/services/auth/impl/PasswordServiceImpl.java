package com.atlanta.banking.identity.service.services.auth.impl;

import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.exception.employee.InvalidPasswordException;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.security.CustomUserDetails;
import com.atlanta.banking.identity.service.services.auth.PasswordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public void changePassword(ChangePasswordRequest request) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Employee employee = userDetails.employee();

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new InvalidPasswordException("New passwords do not match.");
        }

        if (!passwordEncoder.matches(request.currentPassword(), employee.getPassword())) {

            throw new InvalidPasswordException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(request.newPassword(), employee.getPassword())) {

            throw new InvalidPasswordException("New password must be different from the current password.");
        }

        employee.setPassword(passwordEncoder.encode(request.newPassword()));

        employee.setCredentialsExpired(false);

        employeeRepository.save(employee);
    }
}
