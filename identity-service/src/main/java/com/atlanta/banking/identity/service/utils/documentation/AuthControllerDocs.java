package com.atlanta.banking.identity.service.utils.documentation;

import com.atlanta.banking.identity.service.dto.auth.LoginRequest;
import com.atlanta.banking.identity.service.dto.auth.LoginResponse;
import com.atlanta.banking.identity.service.dto.employee.EmployeeProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = """
        Authentication APIs for Atlanta Banking employees.
        
        These endpoints handle employee login,
        JWT token generation and retrieval of the
        currently authenticated employee profile.
        """)
public interface AuthControllerDocs {

    @Operation(summary = "Authenticate employee", description = """
            Authenticates an employee using username and password.
            
            Returns a JWT Bearer token that must be included
            in the Authorization header for secured endpoints.
            """, security = {})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Authentication successful"), @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(examples = @ExampleObject(value = """
            {
              "timestamp": "2026-07-15T10:15:30",
              "status": 400,
              "error": "Bad Request",
              "message": "Validation failed."
            }
            """))), @ApiResponse(responseCode = "401", description = "Invalid username or password"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Employee login credentials", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Login Request", value = """
            {
              "username": "LASKSDLFK",
              "password": "SDGKKSSsd@94665"
            }
            """)))
    ResponseEntity<LoginResponse> login(LoginRequest request);

    @Operation(summary = "Get current employee profile", description = """
            Returns the profile of the currently
            authenticated employee.
            """, security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
              "tokenType": "Bearer",
              "expiresIn": 900,
              "employeeId": "100000001",
              "username": "SDHRHGSHT",
              "fullName": "Sid Saxena",
              "roles": [
                "ROLE_SUPER_ADMIN"
              ],
              "mustChangePassword": false
            }
            """))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Access denied")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Employee information", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Create Employee", value = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "john.doe@atlantabank.com",
              "phoneNumber": "9876543210",
              "department": "IT",
              "designation": "Manager",
              "roles": [
                "ROLE_MANAGER"
              ]
            }
            """)))
    ResponseEntity<EmployeeProfileResponse> me();
}