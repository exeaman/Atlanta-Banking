package com.atlanta.banking.identity.service.dto;

import com.atlanta.banking.identity.service.entity.UserRoles;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class RegisterResponse {
    private UUID id;

    private String username;

    private String password;

    private boolean enabled;

    private Set<UserRoles> roles ;
}
