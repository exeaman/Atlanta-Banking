package com.atlanta.banking.identity.service.security;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    private Employee employee;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {

        employee = new Employee();

        employee.setUsername("john");
        employee.setPassword("password");

        employee.setEnabled(true);
        employee.setAccountExpired(false);
        employee.setAccountLocked(false);
        employee.setCredentialsExpired(false);

        Role admin = Role.builder()
                .name("ROLE_ADMIN")
                .description("Administrator")
                .build();

        Role manager = Role.builder()
                .name("ROLE_MANAGER")
                .description("Manager")
                .build();

        employee.setRoles(Set.of(admin, manager));

        userDetails = new CustomUserDetails(employee);
    }

    @Test
    void shouldReturnUsername() {

        assertEquals("john", userDetails.getUsername());
    }

    @Test
    void shouldReturnPassword() {

        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void shouldReturnAuthorities() {

        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(
                Set.of("ROLE_ADMIN", "ROLE_MANAGER"),
                authorities
        );
    }

    @Test
    void shouldReturnAccountNonExpired() {

        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void shouldReturnFalseWhenAccountExpired() {

        employee.setAccountExpired(true);

        assertFalse(userDetails.isAccountNonExpired());
    }

    @Test
    void shouldReturnAccountNonLocked() {

        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void shouldReturnFalseWhenAccountLocked() {

        employee.setAccountLocked(true);

        assertFalse(userDetails.isAccountNonLocked());
    }

    @Test
    void shouldReturnCredentialsNonExpired() {

        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnFalseWhenCredentialsExpired() {

        employee.setCredentialsExpired(true);

        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnEnabled() {

        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldReturnDisabled() {

        employee.setEnabled(false);

        assertFalse(userDetails.isEnabled());
    }
}