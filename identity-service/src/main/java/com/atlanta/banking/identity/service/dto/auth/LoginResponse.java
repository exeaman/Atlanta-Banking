package com.atlanta.banking.identity.service.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class LoginResponse {

    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long expiresIn;

    private String employeeId;

    private String username;

    private String fullName;

    private Set<String> roles;

    private boolean mustChangePassword;
}