package com.atlanta.banking.identity.service.services.auth.impl;


import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.mapper.AuthMapper;
import com.atlanta.banking.identity.service.security.CustomUserDetails;
import com.atlanta.banking.identity.service.security.JwtService;
import com.atlanta.banking.identity.service.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Employee employee = userDetails.employee();

        String token = jwtService.generateToken(userDetails);

        return authMapper.toLoginResponse(employee, token, jwtExpiration / 1000);
    }

    @Override
    public EmployeeProfileResponse me() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return authMapper.toEmployeeProfileResponse(userDetails.employee());
    }
}
