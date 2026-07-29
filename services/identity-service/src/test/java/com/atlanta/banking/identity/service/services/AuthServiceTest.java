package com.atlanta.banking.identity.service.services;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import com.atlanta.banking.identity.service.mapper.AuthMapper;
import com.atlanta.banking.identity.service.security.CustomUserDetails;
import com.atlanta.banking.identity.service.security.JwtService;
import com.atlanta.banking.identity.service.services.auth.impl.AuthServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private final long jwtExpirationMs = 900000L;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private CustomUserDetails customUserDetails;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private AuthServiceImpl authService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtExpiration", jwtExpirationMs);

        employee = new Employee();
        employee.setUsername("AMTLXSJAL");
        employee.setEmployeeId("100000000");
        employee.setFirstName("Aman");
        employee.setLastName("Jaiswal");
        employee.setEmail("aman.admin@atlantabank.com");
        employee.setPhoneNumber("9876543210");
        employee.setDepartment(Department.IT);
        employee.setDesignation(Designation.SUPER_ADMIN);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("login() Tests")
    class LoginTests {

        @Test
        @DisplayName("Should authenticate user and return LoginResponse when credentials are valid")
        void login_Success() {
            LoginRequest request = LoginRequest.builder()
                    .username("AMTLXSJAL")
                    .password("SuperAdmin@123")
                    .build();

            String mockJwtToken = "mock.jwt.token";
            long expectedExpirationInSeconds = 900L;

            LoginResponse expectedResponse = LoginResponse.builder()
                    .accessToken(mockJwtToken)
                    .tokenType("Bearer")
                    .expiresIn(expectedExpirationInSeconds)
                    .employeeId("100000000")
                    .username("AMTLXSJAL")
                    .fullName("Aman Jaiswal")
                    .roles(Set.of("ROLE_SUPER_ADMIN"))
                    .mustChangePassword(false)
                    .build();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(customUserDetails.employee()).thenReturn(employee);
            when(jwtService.generateToken(customUserDetails)).thenReturn(mockJwtToken);
            when(authMapper.toLoginResponse(employee, mockJwtToken, expectedExpirationInSeconds))
                    .thenReturn(expectedResponse);

            LoginResponse actualResponse = authService.login(request);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);
            assertEquals("Bearer", actualResponse.getTokenType());
            assertEquals(900L, actualResponse.getExpiresIn());

            verify(authenticationManager).authenticate(
                    argThat(token ->
                            token.getPrincipal().equals("AMTLXSJAL") &&
                                    token.getCredentials().equals("SuperAdmin@123")
                    )
            );
            verify(jwtService).generateToken(customUserDetails);
            verify(authMapper).toLoginResponse(employee, mockJwtToken, expectedExpirationInSeconds);
        }

        @Test
        @DisplayName("Should throw BadCredentialsException when AuthenticationManager rejects credentials")
        void login_BadCredentials_ThrowsException() {
            LoginRequest request = LoginRequest.builder()
                    .username("AMTLXSJAL")
                    .password("WrongPassword")
                    .build();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid username or password."));

            assertThrows(BadCredentialsException.class, () -> authService.login(request));

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verifyNoInteractions(jwtService);
            verifyNoInteractions(authMapper);
        }
    }

    @Nested
    @DisplayName("me() Tests")
    class MeTests {

        @Test
        @DisplayName("Should return EmployeeProfileResponse for authenticated user in SecurityContext")
        void me_Success() {
            EmployeeProfileResponse expectedProfile = EmployeeProfileResponse.builder()
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

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(customUserDetails);
            when(customUserDetails.employee()).thenReturn(employee);
            when(authMapper.toEmployeeProfileResponse(employee)).thenReturn(expectedProfile);

            SecurityContextHolder.setContext(securityContext);

            EmployeeProfileResponse actualProfile = authService.me();

            assertNotNull(actualProfile);
            assertEquals(expectedProfile, actualProfile);
            assertEquals("100000000", actualProfile.employeeId());
            assertEquals("Aman Jaiswal", actualProfile.fullName());

            verify(securityContext).getAuthentication();
            verify(customUserDetails).employee();
            verify(authMapper).toEmployeeProfileResponse(employee);
        }
    }
}