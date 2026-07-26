package com.atlanta.banking.audit.service.mapper;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.entity.AuditLog;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AuditMapperImpl implements AuditMapper {

    @Override
    public AuditLog toEntity(AuditEvent event) {

        return AuditLog.builder().eventId(event.eventId()).eventVersion(event.eventVersion()).eventType(event.eventType()).serviceName(event.serviceName()).category(event.category()).action(event.action())

                .entityType(event.entityType()).entityId(event.entityId())

                .performedById(event.performedById())

                .status(event.status()).severity(event.severity()).message(event.message())

                .occurredAt(event.occurredAt()).createdAt(Instant.now())

                .correlationId(event.correlationId() == null ? null : UUID.fromString(event.correlationId())).requestId(event.requestId()).sourceService(event.sourceService())

                .metadata(event.metadata())

                .build();
    }

    @Override
    public AuditResponse toResponse(AuditLog auditLog) {
        return new AuditResponse(auditLog.getEventId(), auditLog.getServiceName(), auditLog.getCategory(), auditLog.getStatus(), auditLog.getEntityType(), auditLog.getEntityId(), auditLog.getPerformedById(), auditLog.getAction(), auditLog.getOccurredAt());
    }
}
