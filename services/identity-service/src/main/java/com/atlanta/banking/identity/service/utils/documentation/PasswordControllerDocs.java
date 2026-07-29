package com.atlanta.banking.identity.service.utils.documentation;

import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Password Management", description = """
        Password management APIs.
        
        Allows authenticated employees
        to securely change their own password.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface PasswordControllerDocs {

    @Operation(summary = "Change password", description = """
            Changes the password of the currently
            authenticated employee.
            
            The current password must be provided
            before a new password can be set.
            """)
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Password changed successfully"), @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(examples = @ExampleObject(value = """
            {
              "timestamp": "2026-07-15T10:15:30",
              "status": 400,
              "error": "Bad Request",
              "message": "Validation failed."
            }
            """))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "Access denied")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Password change request", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Change Password", value = """
            {
              "currentPassword": "OldPassword@123",
              "newPassword": "NewPassword@123",
              "confirmNewPassword": "NewPassword@123"
            }
            """)))
    ResponseEntity<Void> changePassword(ChangePasswordRequest request);
}