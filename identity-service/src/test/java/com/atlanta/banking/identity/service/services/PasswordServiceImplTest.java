package com.atlanta.banking.identity.service.services;

import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.exception.employee.InvalidPasswordException;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.security.CustomUserDetails;
import com.atlanta.banking.identity.service.services.auth.impl.PasswordServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails customUserDetails;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setPassword("encodedOldPassword");
        employee.setCredentialsExpired(true);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.employee()).thenReturn(employee);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should change password successfully when all validations pass")
    void changePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "Current@123",
                "NewPass@123",
                "NewPass@123"
        );

        when(passwordEncoder.matches("Current@123", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.matches("NewPass@123", "encodedOldPassword")).thenReturn(false);
        when(passwordEncoder.encode("NewPass@123")).thenReturn("encodedNewPassword");

        passwordService.changePassword(request);

        assertEquals("encodedNewPassword", employee.getPassword());
        assertFalse(employee.getCredentialsExpired());

        verify(passwordEncoder).matches("Current@123", "encodedOldPassword");
        verify(passwordEncoder).matches("NewPass@123", "encodedOldPassword");
        verify(passwordEncoder).encode("NewPass@123");
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when new password and confirm password do not match")
    void changePassword_PasswordsDoNotMatch_ThrowsException() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "Current@123",
                "NewPass@123",
                "DifferentPass@123"
        );

        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> passwordService.changePassword(request)
        );

        assertEquals("New passwords do not match.", exception.getMessage());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when current password is incorrect")
    void changePassword_IncorrectCurrentPassword_ThrowsException() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "WrongCurrent@123",
                "NewPass@123",
                "NewPass@123"
        );

        when(passwordEncoder.matches("WrongCurrent@123", "encodedOldPassword")).thenReturn(false);

        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> passwordService.changePassword(request)
        );

        assertEquals("Current password is incorrect.", exception.getMessage());
        verify(passwordEncoder).matches("WrongCurrent@123", "encodedOldPassword");
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when new password is the same as current password")
    void changePassword_NewPasswordSameAsCurrent_ThrowsException() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "Current@123",
                "Current@123",
                "Current@123"
        );

        when(passwordEncoder.matches("Current@123", "encodedOldPassword")).thenReturn(true);

        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> passwordService.changePassword(request)
        );

        assertEquals("New password must be different from the current password.", exception.getMessage());
        verify(passwordEncoder, times(2)).matches("Current@123", "encodedOldPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verifyNoInteractions(employeeRepository);
    }
}