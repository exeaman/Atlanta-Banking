package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;
import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import com.atlanta.banking.identity.service.security.CustomUserDetailsService;
import com.atlanta.banking.identity.service.security.JwtService;
import com.atlanta.banking.identity.service.services.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private LoginRequest validLoginRequest;
    private LoginResponse loginResponse;
    private EmployeeProfileResponse employeeProfileResponse;

    @BeforeEach
    void setUp() {

        validLoginRequest = LoginRequest.builder()
                .username("AMTLXSJAL")
                .password("SuperAdmin@123")
                .build();

        loginResponse = LoginResponse.builder()
                .accessToken("mocked.jwt.token")
                .tokenType("Bearer")
                .expiresIn(900L)
                .employeeId("100000000")
                .username("AMTLXSJAL")
                .fullName("Aman Jaiswal")
                .roles(Set.of("ROLE_SUPER_ADMIN"))
                .mustChangePassword(false)
                .build();

        employeeProfileResponse = EmployeeProfileResponse.builder()
                .employeeId("100000000")
                .username("AMTLXSJAL")
                .firstName("Aman")
                .lastName("Jaiswal")
                .fullName("Aman Jaiswal")
                .email("aman.admin@atlantabank.com")
                .phoneNumber("9876543210")
                .department(Department.IT)
                .designation(Designation.SUPER_ADMIN)
                .roles(Set.of("ROLE_SUPER_ADMIN"))
                .enabled(true)
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should return login response when credentials are valid")
        void shouldLoginSuccessfully() throws Exception {

            when(authService.login(any(LoginRequest.class)))
                    .thenReturn(loginResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.accessToken").value("mocked.jwt.token"))
                    .andExpect(jsonPath("$.tokenType").value("Bearer"))
                    .andExpect(jsonPath("$.expiresIn").value(900))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$.fullName").value("Aman Jaiswal"))
                    .andExpect(jsonPath("$.roles[0]").value("ROLE_SUPER_ADMIN"))
                    .andExpect(jsonPath("$.mustChangePassword").value(false));

            verify(authService).login(any(LoginRequest.class));
            verifyNoMoreInteractions(authService);
        }

        @Test
        @DisplayName("Should return 400 when request validation fails")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {

            LoginRequest invalidRequest = LoginRequest.builder()
                    .username("")
                    .password(null)
                    .build();

            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("Should return 400 for malformed JSON")
        void shouldReturnBadRequestForMalformedJson() throws Exception {

            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(authService);
        }

        @Test
        @DisplayName("Should return 401 when credentials are invalid")
        void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {

            when(authService.login(any(LoginRequest.class)))
                    .thenThrow(new BadCredentialsException("Invalid username or password."));

            mockMvc.perform(post("/api/v1/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isUnauthorized());

            verify(authService).login(any(LoginRequest.class));
            verifyNoMoreInteractions(authService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/auth/me")
    class MeTests {

        @Test
        @WithMockUser(username = "AMTLXSJAL", roles = "SUPER_ADMIN")
        @DisplayName("Should return authenticated employee profile")
        void shouldReturnAuthenticatedEmployeeProfile() throws Exception {

            when(authService.me()).thenReturn(employeeProfileResponse);

            mockMvc.perform(get("/api/v1/auth/me")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$.firstName").value("Aman"))
                    .andExpect(jsonPath("$.lastName").value("Jaiswal"))
                    .andExpect(jsonPath("$.fullName").value("Aman Jaiswal"))
                    .andExpect(jsonPath("$.email").value("aman.admin@atlantabank.com"))
                    .andExpect(jsonPath("$.department").value("IT"))
                    .andExpect(jsonPath("$.designation").value("SUPER_ADMIN"))
                    .andExpect(jsonPath("$.enabled").value(true));

            verify(authService).me();
            verifyNoMoreInteractions(authService);
        }
    }
}