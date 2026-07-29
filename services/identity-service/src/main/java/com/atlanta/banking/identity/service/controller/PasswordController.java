package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;
import com.atlanta.banking.identity.service.services.auth.PasswordService;
import com.atlanta.banking.identity.service.utils.documentation.PasswordControllerDocs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController implements PasswordControllerDocs {

    private final PasswordService passwordService;

    @PutMapping
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        passwordService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}