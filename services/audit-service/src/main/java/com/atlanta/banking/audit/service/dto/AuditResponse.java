package com.atlanta.banking.audit.service.dto;

import com.atlanta.banking.audit.service.contract.audit.enums.EventCategory;
import com.atlanta.banking.audit.service.contract.audit.enums.EventStatus;
import com.atlanta.banking.audit.service.contract.audit.enums.ServiceName;

import java.time.Instant;
import java.util.UUID;

public record AuditResponse(

        UUID eventId,

        ServiceName serviceName,

        EventCategory category,

        EventStatus status,

        String entityType,

        String entityId,

        UUID performedById,

        String action,

        Instant occurredAt

) {
}