package com.atlanta.banking.audit.service.mapper;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.enums.AuditAction;
import com.atlanta.banking.audit.service.enums.EntityType;
import com.atlanta.banking.audit.service.event.EmployeeCreatedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class AuditMapperImpl implements AuditMapper {

    @Override
    public AuditLog toEntity(AuditEvent event) {

        return AuditLog.builder().eventId(event.eventId()).eventVersion(event.eventVersion()).eventType(event.eventType()).serviceName(event.serviceName()).category(event.category()).action(event.action())

                .entityType(event.entityType()).entityId(event.entityId())

                .performedById(event.performedById())

                .status(event.status()).severity(event.severity()).message(event.message())

                .occurredAt(event.occurredAt()).createdAt(event.recordedAt())

                .correlationId(event.correlationId() == null ? null : UUID.fromString(event.correlationId())).requestId(event.requestId()).sourceService(event.sourceService())

                .metadata(event.metadata())

                .build();
    }

    @Override
    public AuditResponse toResponse(AuditLog auditLog) {
        return new AuditResponse(auditLog.getEventId(), auditLog.getServiceName(), auditLog.getCategory(), auditLog.getStatus(), auditLog.getEntityType(), auditLog.getEntityId(), auditLog.getPerformedById(), auditLog.getAction(), auditLog.getOccurredAt());
    }

    @Override
    public AuditEvent toEvent(EmployeeCreatedEvent event) {

        return AuditEvent.builder()
                .eventId(event.metadata().eventId())
                .eventVersion(event.metadata().eventVersion())
                .eventType(EventType.AUDIT)

                .serviceName(ServiceName.IDENTITY)
                .category(EventCategory.BUSINESS)
                .action(String.valueOf(AuditAction.EMPLOYEE_CREATED))

                .entityType(String.valueOf(EntityType.EMPLOYEE))
                .entityId(event.employeeId())

                //TODO
                .performedById(null)
                .performedByUsername(null)
                .performedByRoles(null)

                .status(EventStatus.SUCCESS)
                .severity(EventSeverity.INFO)

                .message(String.format(
                        "Employee %s (%s) created successfully",
                        event.employeeId(),
                        event.username()
                ))

                .occurredAt(event.metadata().occurredAt())
                .recordedAt(Instant.now())

                //TODO
                .correlationId(
                        event.metadata().correlationId() == null
                                ? null
                                : event.metadata().correlationId().toString()
                )

                .requestId(null)

                .sourceService("identity-service")

                .metadata(Map.of(
                        "username", event.username(),
                        "firstName", event.firstName(),
                        "lastName", event.lastName(),
                        "email", event.email(),
                        "department", event.department().name()
                ))

                .build();
    }
}
