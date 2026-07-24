package com.atlanta.banking.identity.service.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(

        @NotBlank String currentPassword,

        @NotBlank @Size(min = 8, max = 100) String newPassword,

        @NotBlank @Size(min = 8, max = 100) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$", message = "Password must contain uppercase, lowercase, number and special character.") String confirmNewPassword

) {
}