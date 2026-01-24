package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.LoginRequest;
import com.atlanta.banking.identity.service.dto.LoginResponse;
import com.atlanta.banking.identity.service.dto.RegisterRequest;
import com.atlanta.banking.identity.service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(
                request.username(), request.password());
        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest registerRequest){
        return  null;
    }
}
