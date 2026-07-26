package com.atlanta.banking.audit.service.config.openapi;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Audit Events",
        description = "Operations for recording and querying audit events generated across Atlanta Banking services."
)
public interface AuditApi {

    @Operation(
            summary = "Record an audit event",
            description = "Persists a new audit event received from another banking service."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Audit event recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid audit event",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Duplicate audit event",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Void> recordEvent(
            @Valid
            @RequestBody
            AuditEvent event
    );

    @Operation(
            summary = "Retrieve audit events",
            description = "Returns paginated audit events. Results can be filtered using the supplied search criteria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Audit events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<AuditResponse>> getAuditEvents(

            @Parameter(
                    description = "Filtering criteria used to search audit events."
            )
            @ParameterObject
            AuditSearchCriteria criteria,

            @ParameterObject
            Pageable pageable
    );
}