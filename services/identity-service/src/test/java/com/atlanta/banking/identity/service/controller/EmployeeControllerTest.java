package com.atlanta.banking.identity.service.controller;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import com.atlanta.banking.identity.service.security.CustomUserDetailsService;
import com.atlanta.banking.identity.service.security.JwtService;
import com.atlanta.banking.identity.service.services.employee.EmployeeService;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private UUID systemId;

    private CreateEmployeeRequest createEmployeeRequest;
    private UpdateEmployeeRequest updateEmployeeRequest;

    private CreateEmployeeResponse createEmployeeResponse;
    private EmployeeResponse employeeResponse;

    private List<EmployeeResponse> employeeResponses;

    @BeforeEach
    void setUp() {

        systemId = UUID.randomUUID();

        createEmployeeRequest = CreateEmployeeRequest.builder()
                .firstName("Aman")
                .lastName("Jaiswal")
                .email("aman.admin@atlantabank.com")
                .phoneNumber("9876543210")
                .department(Department.IT)
                .designation(Designation.SUPER_ADMIN)
                .roles(Set.of("ROLE_SUPER_ADMIN"))
                .build();

        updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName("Aman")
                .lastName("Sharma")
                .email("aman.sharma@atlantabank.com")
                .phoneNumber("9876543211")
                .department(Department.IT)
                .designation(Designation.ADMIN)
                .build();

        createEmployeeResponse = CreateEmployeeResponse.builder()
                .systemId(systemId)
                .employeeId("100000000")
                .username("AMTLXSJAL")
                .firstName("Aman")
                .lastName("Jaiswal")
                .email("aman.admin@atlantabank.com")
                .phoneNumber("9876543210")
                .department(Department.IT)
                .designation(Designation.SUPER_ADMIN)
                .enabled(true)
                .employed(true)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .roles(Set.of("ROLE_SUPER_ADMIN"))
                .temporaryPassword("Temp@123")
                .build();

        employeeResponse = EmployeeResponse.builder()
                .systemId(systemId)
                .employeeId("100000000")
                .username("AMTLXSJAL")
                .firstName("Aman")
                .lastName("Jaiswal")
                .email("aman.admin@atlantabank.com")
                .phoneNumber("9876543210")
                .department(Department.IT)
                .designation(Designation.SUPER_ADMIN)
                .roles(Set.of("ROLE_SUPER_ADMIN"))
                .enabled(true)
                .build();

        employeeResponses = List.of(employeeResponse);
    }

    @Nested
    @DisplayName("POST /api/v1/employees")
    class CreateEmployeeTests {

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should create employee successfully")
        void shouldCreateEmployeeSuccessfully() throws Exception {

            when(employeeService.createEmployee(any(CreateEmployeeRequest.class)))
                    .thenReturn(createEmployeeResponse);
            System.out.println(objectMapper.writeValueAsString(createEmployeeRequest));
            mockMvc.perform(post("/api/v1/employees")
                            .with(csrf())
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createEmployeeRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(String.valueOf(APPLICATION_JSON)))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$.firstName").value("Aman"))
                    .andExpect(jsonPath("$.lastName").value("Jaiswal"))
                    .andExpect(jsonPath("$.email").value("aman.admin@atlantabank.com"))
                    .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                    .andExpect(jsonPath("$.department").value("IT"))
                    .andExpect(jsonPath("$.designation").value("SUPER_ADMIN"))
                    .andExpect(jsonPath("$.enabled").value(true))
                    .andExpect(jsonPath("$.employed").value(true))
                    .andExpect(jsonPath("$.accountLocked").value(false))
                    .andExpect(jsonPath("$.accountExpired").value(false))
                    .andExpect(jsonPath("$.credentialsExpired").value(false))
                    .andExpect(jsonPath("$.roles[0]").value("ROLE_SUPER_ADMIN"))
                    .andExpect(jsonPath("$.temporaryPassword").value("Temp@123"));

            verify(employeeService).createEmployee(any(CreateEmployeeRequest.class));
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should return 400 when create employee request is invalid")
        void shouldReturnBadRequestWhenCreateEmployeeRequestIsInvalid() throws Exception {

            CreateEmployeeRequest invalidRequest = CreateEmployeeRequest.builder()
                    .firstName("")
                    .lastName("")
                    .email("invalid-email")
                    .phoneNumber("12345")
                    .department(null)
                    .designation(null)
                    .roles(Set.of())
                    .build();

            mockMvc.perform(post("/api/v1/employees")
                            .with(csrf())
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(employeeService);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/employees")
    class GetEmployeeTests {

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should return employee by system id")
        void shouldGetEmployeeBySystemId() throws Exception {

            when(employeeService.getEmployeeBySystemId(systemId))
                    .thenReturn(employeeResponse);

            mockMvc.perform(get("/api/v1/employees/{systemId}", systemId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$.firstName").value("Aman"))
                    .andExpect(jsonPath("$.lastName").value("Jaiswal"));

            verify(employeeService).getEmployeeBySystemId(systemId);
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should return employee by employee id")
        void shouldGetEmployeeByEmployeeId() throws Exception {

            when(employeeService.getEmployeeByEmployeeId("100000000"))
                    .thenReturn(employeeResponse);

            mockMvc.perform(get("/api/v1/employees/employee-id/{employeeId}", "100000000"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"));

            verify(employeeService).getEmployeeByEmployeeId("100000000");
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should return all employees")
        void shouldReturnAllEmployees() throws Exception {

            when(employeeService.findEmployees())
                    .thenReturn(employeeResponses);

            mockMvc.perform(get("/api/v1/employees"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].employeeId").value("100000000"))
                    .andExpect(jsonPath("$[0].username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$[0].firstName").value("Aman"));

            verify(employeeService).findEmployees();
            verifyNoMoreInteractions(employeeService);
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/employees")
    class UpdateEmployeeTests {

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should update employee successfully")
        void shouldUpdateEmployeeSuccessfully() throws Exception {

            when(employeeService.updateEmployee(eq(systemId), any(UpdateEmployeeRequest.class)))
                    .thenReturn(employeeResponse);

            mockMvc.perform(put("/api/v1/employees/{systemId}", systemId)
                            .with(csrf())
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateEmployeeRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.username").value("AMTLXSJAL"))
                    .andExpect(jsonPath("$.firstName").value("Aman"))
                    .andExpect(jsonPath("$.lastName").value("Jaiswal"));

            verify(employeeService).updateEmployee(eq(systemId), any(UpdateEmployeeRequest.class));
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should return 400 when update employee request is invalid")
        void shouldReturnBadRequestWhenUpdateEmployeeRequestIsInvalid() throws Exception {

            UpdateEmployeeRequest invalidRequest = UpdateEmployeeRequest.builder()
                    .firstName("")
                    .lastName("")
                    .email("invalid-email")
                    .phoneNumber("12345")
                    .department(null)
                    .designation(null)
                    .build();

            mockMvc.perform(put("/api/v1/employees/{systemId}", systemId)
                            .with(csrf())
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(employeeService);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/employees")
    class EmployeeStatusTests {

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should enable employee successfully")
        void shouldEnableEmployeeSuccessfully() throws Exception {

            when(employeeService.enableEmployee(systemId))
                    .thenReturn(employeeResponse);

            mockMvc.perform(patch("/api/v1/employees/{systemId}/enable", systemId)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.employeeId").value("100000000"))
                    .andExpect(jsonPath("$.enabled").value(true));

            verify(employeeService).enableEmployee(systemId);
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should disable employee successfully")
        void shouldDisableEmployeeSuccessfully() throws Exception {

            EmployeeResponse disabledEmployee = EmployeeResponse.builder()
                    .systemId(systemId)
                    .employeeId("100000000")
                    .username("AMTLXSJAL")
                    .firstName("Aman")
                    .lastName("Jaiswal")
                    .email("aman.admin@atlantabank.com")
                    .phoneNumber("9876543210")
                    .department(Department.IT)
                    .designation(Designation.SUPER_ADMIN)
                    .enabled(false)
                    .employed(true)
                    .accountLocked(false)
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .roles(Set.of("ROLE_SUPER_ADMIN"))
                    .build();

            when(employeeService.disableEmployee(systemId))
                    .thenReturn(disabledEmployee);

            mockMvc.perform(patch("/api/v1/employees/{systemId}/disable", systemId)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.enabled").value(false));

            verify(employeeService).disableEmployee(systemId);
            verifyNoMoreInteractions(employeeService);
        }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
        @DisplayName("Should terminate employee successfully")
        void shouldTerminateEmployeeSuccessfully() throws Exception {

            EmployeeResponse terminatedEmployee = EmployeeResponse.builder()
                    .systemId(systemId)
                    .employeeId("100000000")
                    .username("AMTLXSJAL")
                    .firstName("Aman")
                    .lastName("Jaiswal")
                    .email("aman.admin@atlantabank.com")
                    .phoneNumber("9876543210")
                    .department(Department.IT)
                    .designation(Designation.SUPER_ADMIN)
                    .enabled(false)
                    .employed(false)
                    .accountLocked(false)
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .roles(Set.of("ROLE_SUPER_ADMIN"))
                    .build();

            when(employeeService.terminateEmployee(systemId))
                    .thenReturn(terminatedEmployee);

            mockMvc.perform(patch("/api/v1/employees/{systemId}/terminate", systemId)
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.systemId").value(systemId.toString()))
                    .andExpect(jsonPath("$.employed").value(false));

            verify(employeeService).terminateEmployee(systemId);
            verifyNoMoreInteractions(employeeService);
        }
    }
}