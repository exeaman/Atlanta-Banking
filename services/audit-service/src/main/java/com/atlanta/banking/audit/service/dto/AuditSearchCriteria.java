package com.atlanta.banking.audit.service.dto;

import com.atlanta.banking.audit.service.contract.audit.enums.EventCategory;
import com.atlanta.banking.audit.service.contract.audit.enums.EventStatus;
import com.atlanta.banking.audit.service.contract.audit.enums.ServiceName;

import java.time.Instant;
import java.util.UUID;

public record AuditSearchCriteria(
        ServiceName serviceName,
        EventStatus status,
        EventCategory category,
        UUID performedById,
        String entityType,
        String entityId,
        Instant occurredAfter,
        Instant occurredBefore
) {
}