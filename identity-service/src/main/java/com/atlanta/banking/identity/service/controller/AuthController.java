package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;
import com.atlanta.banking.identity.service.services.auth.AuthService;
import com.atlanta.banking.identity.service.utils.documentation.AuthControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeProfileResponse> me() {
        return ResponseEntity.ok(authService.me());
    }
}