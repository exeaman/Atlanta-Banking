package com.atlanta.banking.identity.service.services;


import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.security.CustomUserDetails;
import com.atlanta.banking.identity.service.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldLoadUserByUsername() {

        Employee employee = Employee.builder()
                .username("john")
                .password("password")
                .enabled(true)
                .roles(Set.of(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build()
                ))
                .build();

        when(employeeRepository.findWithRolesByUsername("john"))
                .thenReturn(Optional.of(employee));

        UserDetails result =
                customUserDetailsService.loadUserByUsername("john");

        assertNotNull(result);
        assertInstanceOf(CustomUserDetails.class, result);

        assertEquals("john", result.getUsername());
        assertEquals("password", result.getPassword());

        verify(employeeRepository)
                .findWithRolesByUsername("john");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findWithRolesByUsername("john"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("john")
        );

        assertEquals(
                "Invalid username or password.john",
                exception.getMessage()
        );

        verify(employeeRepository)
                .findWithRolesByUsername("john");
    }
}