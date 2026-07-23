package com.atlanta.banking.identity.service.security;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record CustomUserDetails(Employee employee) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return employee.getRoles()
                .stream()
                .map(Role::getName)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !employee.getAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !employee.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !employee.getCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return employee.getEnabled();
    }
}