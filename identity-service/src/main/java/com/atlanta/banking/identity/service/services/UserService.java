package com.atlanta.banking.identity.service.services;

import com.atlanta.banking.identity.service.dto.RegisterRequest;
import com.atlanta.banking.identity.service.dto.RegisterResponse;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest registerRequest);
}
