package com.atlanta.banking.identity.service.services;

import com.atlanta.banking.identity.service.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationConfiguration authConfig, JwtUtil jwtUtil)
            throws Exception {
        this.authenticationManager = authConfig.getAuthenticationManager();
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        return jwtUtil.generateToken(
                (UserDetails) authentication.getPrincipal()
        );
    }
}
