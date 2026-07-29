package com.atlanta.banking.identity.service.utils.documentation;


import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeRequest;
import com.atlanta.banking.identity.service.dto.employee.CreateEmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeResponse;
import com.atlanta.banking.identity.service.dto.employee.UpdateEmployeeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Employee Management", description = """
        Administrative APIs for managing Atlanta Banking employees.
        
        These endpoints allow authorized administrators to create,
        retrieve, update and manage employee accounts.
        
        Access is restricted based on employee roles using
        Role-Based Access Control (RBAC).
        """)
@SecurityRequirement(name = "bearerAuth")
public interface EmployeeControllerDocs {

    @Operation(summary = "Create employee", description = """
            Creates a new employee account.
            
            Generates a unique employee ID, username and temporary
            credentials, assigns the requested roles and persists
            the employee record.
            
            Accessible only to administrators.
            """)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Employee created successfully"), @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(examples = @ExampleObject(value = """
            {
              "timestamp": "2026-07-15T10:15:30",
              "status": 400,
              "error": "Bad Request",
              "message": "Validation failed."
            }
            """))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "409", description = "Employee already exists"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    ResponseEntity<CreateEmployeeResponse> createEmployee(CreateEmployeeRequest request);

    @Operation(summary = "Get employee by system ID", description = """
            Retrieves a single employee using the internal
            system UUID.
            
            Intended for administrative operations.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee retrieved successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found")})
    ResponseEntity<EmployeeResponse> getEmployeeBySystemId(UUID systemId);

    @Operation(summary = "Get employee by employee ID", description = """
            Retrieves an employee using the official
            bank employee ID.
            
            Employee IDs are unique across the organization.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee retrieved successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found")})
    ResponseEntity<EmployeeResponse> getEmployeeByEmployeeId(String employeeId);

    @Operation(summary = "Search employees", description = """
            Returns all employees.
            
            Future versions may support filtering,
            sorting and pagination.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employees retrieved successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions")})
    ResponseEntity<List<EmployeeResponse>> findEmployees();

    @Operation(summary = "Update employee", description = """
            Updates editable employee information.
            
            Security-sensitive attributes such as passwords
            are managed through dedicated endpoints.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee updated successfully"), @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(examples = @ExampleObject(value = """
            {
              "timestamp": "2026-07-15T10:15:30",
              "status": 400,
              "error": "Bad Request",
              "message": "Validation failed."
            }
            """))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found"), @ApiResponse(responseCode = "409", description = "Email or username already exists")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Updated employee details", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Update Employee", value = """
            {
              "email": "john.doe@atlantabank.com",
              "phoneNumber": "9876543210",
              "department": "IT",
              "designation": "Senior Manager",
              "roles": [
                "ROLE_MANAGER",
                "ROLE_SECURITY_ADMIN"
              ]
            }
            """)))
    ResponseEntity<EmployeeResponse> updateEmployee(UUID systemId, UpdateEmployeeRequest request);

    @Operation(summary = "Enable employee", description = """
            Enables a previously disabled employee account.
            
            Enabled employees can authenticate and access
            authorized banking systems.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee enabled successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found")})
    ResponseEntity<EmployeeResponse> enableEmployee(UUID systemId);

    @Operation(summary = "Disable employee", description = """
            Disables an employee account.
            
            Disabled employees are prevented from
            authenticating until re-enabled.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee disabled successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found")})
    ResponseEntity<EmployeeResponse> disableEmployee(UUID systemId);

    @Operation(summary = "Terminate employee", description = """
            Permanently terminates an employee.
            
            This operation marks the employee as no longer employed
            and revokes future access to the banking platform.
            
            Reserved for Super Administrators.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee terminated successfully"), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Insufficient permissions"), @ApiResponse(responseCode = "404", description = "Employee not found")})
    ResponseEntity<EmployeeResponse> terminateEmployee(UUID systemId);
}