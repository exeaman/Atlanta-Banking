package com.atlanta.banking.identity.service.services.auth;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    EmployeeProfileResponse me();

}