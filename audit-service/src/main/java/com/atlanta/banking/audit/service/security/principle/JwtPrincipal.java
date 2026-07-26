package com.atlanta.banking.audit.service.security.principle;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record JwtPrincipal(
        String employeeId,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
}