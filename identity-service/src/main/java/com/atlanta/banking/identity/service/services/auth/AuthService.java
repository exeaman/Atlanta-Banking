package com.atlanta.banking.identity.service.services.auth;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}