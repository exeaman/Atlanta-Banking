package com.atlanta.banking.identity.service.services;

import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import com.atlanta.banking.identity.service.exception.common.DuplicateResourceException;
import com.atlanta.banking.identity.service.exception.common.ResourceNotFoundException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyDisabledException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyEnabledException;
import com.atlanta.banking.identity.service.exception.employee.EmployeeAlreadyTerminatedException;
import com.atlanta.banking.identity.service.exception.role.InvalidRoleAssignmentException;
import com.atlanta.banking.identity.service.mapper.EmployeeMapper;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.repository.RoleRepository;
import com.atlanta.banking.identity.service.services.employee.impl.EmployeeServiceImpl;
import com.atlanta.banking.identity.service.utils.generator.EmployeeIdGenerator;
import com.atlanta.banking.identity.service.utils.generator.TemporaryPasswordGenerator;
import com.atlanta.banking.identity.service.utils.generator.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeIdGenerator employeeIdGenerator;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private TemporaryPasswordGenerator temporaryPasswordGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private UUID systemId;
    private Employee employee;
    private Role defaultRole;

    @BeforeEach
    void setUp() {
        systemId = UUID.randomUUID();

        defaultRole = Role.builder()
                .id(1L)
                .name("ROLE_TELLER")
                .description("Teller Role")
                .build();

        employee = Employee.builder()
                .systemId(systemId)
                .employeeId("100000000")
                .username("AMTLXSJAL")
                .firstName("Aman")
                .lastName("Jaiswal")
                .email("aman@atlantabank.com")
                .phoneNumber("9876543210")
                .department(Department.IT)
                .designation(Designation.SUPER_ADMIN)
                .enabled(true)
                .employed(true)
                .roles(Set.of(defaultRole))
                .build();
    }

    @Nested
    @DisplayName("createEmployee() Tests")
    class CreateEmployeeTests {

        @Test
        @DisplayName("Should create employee successfully when request is valid")
        void createEmployee_Success() {
            CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("new@atlantabank.com");
            when(request.getPhoneNumber()).thenReturn("9876543210");
            when(request.getFirstName()).thenReturn("Aman");
            when(request.getLastName()).thenReturn("Jaiswal");
            when(request.getRoles()).thenReturn(Set.of("ROLE_TELLER"));

            when(employeeRepository.existsByEmail("new@atlantabank.com")).thenReturn(false);
            when(employeeRepository.existsByPhoneNumber("9876543210")).thenReturn(false);
            when(employeeMapper.toEntity(request)).thenReturn(employee);
            when(employeeIdGenerator.generate()).thenReturn("100000000");

            when(usernameGenerator.generate("Aman", "Jaiswal"))
                    .thenReturn("AMTLXSJAL_TAKEN", "AMTLXSJAL");
            when(employeeRepository.existsByUsername("AMTLXSJAL_TAKEN")).thenReturn(true);
            when(employeeRepository.existsByUsername("AMTLXSJAL")).thenReturn(false);

            when(temporaryPasswordGenerator.generate()).thenReturn("TempPass@123");
            when(passwordEncoder.encode("TempPass@123")).thenReturn("encodedTempPass");
            when(roleRepository.findAllByNameIn(Set.of("ROLE_TELLER"))).thenReturn(List.of(defaultRole));
            when(employeeRepository.save(employee)).thenReturn(employee);

            CreateEmployeeResponse expectedResponse = mock(CreateEmployeeResponse.class);
            when(employeeMapper.toCreateResponse(employee, "TempPass@123")).thenReturn(expectedResponse);

            CreateEmployeeResponse actualResponse = employeeService.createEmployee(request);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);

            verify(employeeRepository).existsByEmail("new@atlantabank.com");
            verify(employeeRepository).existsByPhoneNumber("9876543210");
            verify(usernameGenerator, times(2)).generate("Aman", "Jaiswal");
            verify(passwordEncoder).encode("TempPass@123");
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when email already exists")
        void createEmployee_DuplicateEmail_ThrowsException() {
            CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("existing@atlantabank.com");

            when(employeeRepository.existsByEmail("existing@atlantabank.com")).thenReturn(true);

            DuplicateResourceException ex = assertThrows(
                    DuplicateResourceException.class,
                    () -> employeeService.createEmployee(request)
            );

            assertEquals("Email already exists.", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when phone number already exists")
        void createEmployee_DuplicatePhoneNumber_ThrowsException() {
            CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("new@atlantabank.com");
            when(request.getPhoneNumber()).thenReturn("9876543210");

            when(employeeRepository.existsByEmail("new@atlantabank.com")).thenReturn(false);
            when(employeeRepository.existsByPhoneNumber("9876543210")).thenReturn(true);

            DuplicateResourceException ex = assertThrows(
                    DuplicateResourceException.class,
                    () -> employeeService.createEmployee(request)
            );

            assertEquals("Phone number already exists.", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw InvalidRoleAssignmentException when requested role count does not match database roles found")
        void createEmployee_InvalidRole_ThrowsException() {
            CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("new@atlantabank.com");
            when(request.getPhoneNumber()).thenReturn("9876543210");
            when(request.getFirstName()).thenReturn("Aman");
            when(request.getLastName()).thenReturn("Jaiswal");
            when(request.getRoles()).thenReturn(Set.of("ROLE_TELLER", "ROLE_INVALID"));

            when(employeeRepository.existsByEmail("new@atlantabank.com")).thenReturn(false);
            when(employeeRepository.existsByPhoneNumber("9876543210")).thenReturn(false);
            when(employeeMapper.toEntity(request)).thenReturn(employee);
            when(employeeIdGenerator.generate()).thenReturn("100000000");
            when(usernameGenerator.generate("Aman", "Jaiswal")).thenReturn("AMTLXSJAL");
            when(employeeRepository.existsByUsername("AMTLXSJAL")).thenReturn(false);

            when(temporaryPasswordGenerator.generate()).thenReturn("TempPass@123");
            when(passwordEncoder.encode("TempPass@123")).thenReturn("encodedTempPass");

            when(roleRepository.findAllByNameIn(Set.of("ROLE_TELLER", "ROLE_INVALID")))
                    .thenReturn(List.of(defaultRole));

            InvalidRoleAssignmentException ex = assertThrows(
                    InvalidRoleAssignmentException.class,
                    () -> employeeService.createEmployee(request)
            );

            assertEquals("One or more roles are invalid.", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Fetch Employee Tests")
    class FetchEmployeeTests {

        @Test
        @DisplayName("Should return EmployeeResponse when valid system ID is provided")
        void getEmployeeBySystemId_Success() {
            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.getEmployeeBySystemId(systemId);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when system ID does not exist")
        void getEmployeeBySystemId_NotFound_ThrowsException() {
            when(employeeRepository.findById(systemId)).thenReturn(Optional.empty());

            ResourceNotFoundException ex = assertThrows(
                    ResourceNotFoundException.class,
                    () -> employeeService.getEmployeeBySystemId(systemId)
            );

            assertEquals("Employee with system ID " + systemId + " not found.", ex.getMessage());
        }

        @Test
        @DisplayName("Should return EmployeeResponse when valid employee ID string is provided")
        void getEmployeeByEmployeeId_Success() {
            String empId = "100000000";
            when(employeeRepository.findByEmployeeId(empId)).thenReturn(Optional.of(employee));

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.getEmployeeByEmployeeId(empId);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when employee ID string is not found")
        void getEmployeeByEmployeeId_NotFound_ThrowsException() {
            String empId = "999999999";
            when(employeeRepository.findByEmployeeId(empId)).thenReturn(Optional.empty());

            ResourceNotFoundException ex = assertThrows(
                    ResourceNotFoundException.class,
                    () -> employeeService.getEmployeeByEmployeeId(empId)
            );

            assertEquals("Employee with employee ID " + empId + " not found.", ex.getMessage());
        }

        @Test
        @DisplayName("Should return list of all EmployeeResponses")
        void findEmployees_Success() {
            List<Employee> employeeList = List.of(employee);
            EmployeeResponse response = mock(EmployeeResponse.class);

            when(employeeRepository.findAll()).thenReturn(employeeList);
            when(employeeMapper.toResponseList(employeeList)).thenReturn(List.of(response));

            List<EmployeeResponse> actualList = employeeService.findEmployees();

            assertNotNull(actualList);
            assertEquals(1, actualList.size());
            verify(employeeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("updateEmployee() Tests")
    class UpdateEmployeeTests {

        @Test
        @DisplayName("Should update employee details successfully when email and phone are unchanged")
        void updateEmployee_SameEmailAndPhone_Success() {
            UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("aman@atlantabank.com"); // Unchanged
            when(request.getPhoneNumber()).thenReturn("9876543210");   // Unchanged
            when(request.getFirstName()).thenReturn("Aman Updated");
            when(request.getLastName()).thenReturn("Jaiswal Updated");
            when(request.getDepartment()).thenReturn(Department.OPERATIONS);
            when(request.getDesignation()).thenReturn(Designation.MANAGER);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.updateEmployee(systemId, request);

            assertNotNull(actualResponse);
            assertEquals("Aman Updated", employee.getFirstName());
            assertEquals("Jaiswal Updated", employee.getLastName());
            assertEquals(Department.OPERATIONS, employee.getDepartment());
            assertEquals(Designation.MANAGER, employee.getDesignation());

            verify(employeeRepository, never()).existsByEmail(anyString());
            verify(employeeRepository, never()).existsByPhoneNumber(anyString());
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should update employee details when changing to a new available email and phone")
        void updateEmployee_NewUniqueEmailAndPhone_Success() {
            UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("new.email@atlantabank.com");
            when(request.getPhoneNumber()).thenReturn("1122334455");

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsByEmail("new.email@atlantabank.com")).thenReturn(false);
            when(employeeRepository.existsByPhoneNumber("1122334455")).thenReturn(false);
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.updateEmployee(systemId, request);

            assertNotNull(actualResponse);
            assertEquals("new.email@atlantabank.com", employee.getEmail());
            assertEquals("1122334455", employee.getPhoneNumber());

            verify(employeeRepository).existsByEmail("new.email@atlantabank.com");
            verify(employeeRepository).existsByPhoneNumber("1122334455");
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when updating to an existing email")
        void updateEmployee_DuplicateEmail_ThrowsException() {
            UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("taken@atlantabank.com");

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsByEmail("taken@atlantabank.com")).thenReturn(true);

            DuplicateResourceException ex = assertThrows(
                    DuplicateResourceException.class,
                    () -> employeeService.updateEmployee(systemId, request)
            );

            assertEquals("Email already exists.", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when updating to an existing phone number")
        void updateEmployee_DuplicatePhoneNumber_ThrowsException() {
            UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
            when(request.getEmail()).thenReturn("aman@atlantabank.com"); // Unchanged
            when(request.getPhoneNumber()).thenReturn("9999999999");    // Changed to existing

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsByPhoneNumber("9999999999")).thenReturn(true);

            DuplicateResourceException ex = assertThrows(
                    DuplicateResourceException.class,
                    () -> employeeService.updateEmployee(systemId, request)
            );

            assertEquals("Phone number already exists.", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Employee Status Management Tests")
    class StatusManagementTests {

        @Test
        @DisplayName("Should enable a disabled employee successfully")
        void enableEmployee_Success() {
            employee.setEnabled(false);
            employee.setEmployed(true);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.enableEmployee(systemId);

            assertNotNull(actualResponse);
            assertTrue(employee.getEnabled());
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw EmployeeAlreadyTerminatedException when attempting to enable a terminated employee")
        void enableEmployee_Terminated_ThrowsException() {
            employee.setEmployed(false);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeAlreadyTerminatedException ex = assertThrows(
                    EmployeeAlreadyTerminatedException.class,
                    () -> employeeService.enableEmployee(systemId)
            );

            assertEquals("This employee has been terminated!", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmployeeAlreadyEnabledException when employee is already enabled")
        void enableEmployee_AlreadyEnabled_ThrowsException() {
            employee.setEmployed(true);
            employee.setEnabled(true);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeAlreadyEnabledException ex = assertThrows(
                    EmployeeAlreadyEnabledException.class,
                    () -> employeeService.enableEmployee(systemId)
            );

            assertEquals("This employee is already enabled!", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should disable an enabled employee successfully")
        void disableEmployee_Success() {
            employee.setEmployed(true);
            employee.setEnabled(true);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.disableEmployee(systemId);

            assertNotNull(actualResponse);
            assertFalse(employee.getEnabled());
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw EmployeeAlreadyTerminatedException when attempting to disable a terminated employee")
        void disableEmployee_Terminated_ThrowsException() {
            employee.setEmployed(false);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeAlreadyTerminatedException ex = assertThrows(
                    EmployeeAlreadyTerminatedException.class,
                    () -> employeeService.disableEmployee(systemId)
            );

            assertEquals("This employee has been terminated!", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmployeeAlreadyDisabledException when employee is already disabled")
        void disableEmployee_AlreadyDisabled_ThrowsException() {
            employee.setEmployed(true);
            employee.setEnabled(false);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeAlreadyDisabledException ex = assertThrows(
                    EmployeeAlreadyDisabledException.class,
                    () -> employeeService.disableEmployee(systemId)
            );

            assertEquals("This employee is already disabled!", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should terminate an active employee successfully and disable account")
        void terminateEmployee_Success() {
            employee.setEmployed(true);
            employee.setEnabled(true);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));
            when(employeeRepository.save(employee)).thenReturn(employee);

            EmployeeResponse expectedResponse = mock(EmployeeResponse.class);
            when(employeeMapper.toResponse(employee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.terminateEmployee(systemId);

            assertNotNull(actualResponse);
            assertFalse(employee.getEmployed());
            assertFalse(employee.getEnabled());
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw EmployeeAlreadyTerminatedException when employee is already terminated")
        void terminateEmployee_AlreadyTerminated_ThrowsException() {
            employee.setEmployed(false);

            when(employeeRepository.findById(systemId)).thenReturn(Optional.of(employee));

            EmployeeAlreadyTerminatedException ex = assertThrows(
                    EmployeeAlreadyTerminatedException.class,
                    () -> employeeService.terminateEmployee(systemId)
            );

            assertEquals("This employee has already been terminated!", ex.getMessage());
            verify(employeeRepository, never()).save(any());
        }
    }
}