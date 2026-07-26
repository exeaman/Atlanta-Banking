package com.atlanta.banking.audit.service.controller;

import com.atlanta.banking.audit.service.config.openapi.AuditApi;
import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.services.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit/events")
@RequiredArgsConstructor
public class AuditController implements AuditApi {

    private final AuditService auditService;

    @PostMapping
    public ResponseEntity<Void> recordEvent(
            @Valid @RequestBody AuditEvent event) {

        auditService.recordEvent(event);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AuditResponse>> getAuditEvents(
            AuditSearchCriteria criteria,
            Pageable pageable) {

        return ResponseEntity.ok(
                auditService.getAuditEvents(criteria, pageable)
        );
    }
}
