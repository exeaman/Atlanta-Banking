package com.atlanta.banking.audit.service.contract.audit;

import com.atlanta.banking.audit.service.contract.audit.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Builder
public record AuditEvent(

        @NotNull
        UUID eventId,
        Integer eventVersion,
        @NotNull
        EventType eventType,
        @NotNull
        ServiceName serviceName,
        @NotNull
        EventCategory category,
        @NotBlank
        String action,

        @NotBlank
        String entityType,
        @NotBlank
        String entityId,

        UUID performedById,
        String performedByUsername,
        Set<String> performedByRoles,

        @NotNull
        EventStatus status,
        @NotNull
        EventSeverity severity,
        String message,

        @NotNull
        Instant occurredAt,
        Instant recordedAt,

        String correlationId,
        String requestId,
        @NotBlank
        String sourceService,

        Map<String, Object> metadata

) {
}