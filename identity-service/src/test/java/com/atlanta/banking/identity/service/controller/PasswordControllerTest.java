package com.atlanta.banking.identity.service.controller;


import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;
import com.atlanta.banking.identity.service.security.CustomUserDetailsService;
import com.atlanta.banking.identity.service.security.JwtService;
import com.atlanta.banking.identity.service.services.auth.PasswordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PasswordController.class)
@AutoConfigureMockMvc(addFilters = false)
class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PasswordService passwordService;

    // Required only because the security configuration creates the JWT filter
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private ChangePasswordRequest validRequest;

    @BeforeEach
    void setUp() {

        validRequest = ChangePasswordRequest.builder()
                .currentPassword("OldPassword@123")
                .newPassword("NewPassword@123")
                .confirmNewPassword("NewPassword@123")
                .build();
    }

    @Nested
    @DisplayName("PUT /api/v1/password")
    class ChangePasswordTests {

        @Test
        @WithMockUser(username = "AMTLXSJAL", roles = "SUPER_ADMIN")
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() throws Exception {

            doNothing().when(passwordService).changePassword(validRequest);

            mockMvc.perform(put("/api/v1/password")
                            .with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isNoContent());

            verify(passwordService).changePassword(validRequest);
            verifyNoMoreInteractions(passwordService);
        }

        @Test
        @WithMockUser(username = "AMTLXSJAL", roles = "SUPER_ADMIN")
        @DisplayName("Should return 400 when request validation fails")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {

            ChangePasswordRequest invalidRequest = ChangePasswordRequest.builder()
                    .currentPassword("")
                    .newPassword("short")
                    .confirmNewPassword("invalid")
                    .build();

            mockMvc.perform(put("/api/v1/password")
                            .with(csrf())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(passwordService);
        }
    }
}
