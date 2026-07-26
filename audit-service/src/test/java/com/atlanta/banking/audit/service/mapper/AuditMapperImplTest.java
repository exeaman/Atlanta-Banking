package com.atlanta.banking.audit.service.mapper;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.entity.AuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuditMapperImplTest {

    private AuditMapperImpl auditMapper;

    @BeforeEach
    void setUp() {
        auditMapper = new AuditMapperImpl();
    }

    @Test
    void shouldMapAuditEventToAuditLog() {

        AuditEvent event = createAuditEvent();

        AuditLog auditLog = auditMapper.toEntity(event);

        assertThat(auditLog).isNotNull();
        assertThat(auditLog.getEventId()).isEqualTo(event.eventId());
        assertThat(auditLog.getServiceName()).isEqualTo(event.serviceName());
        assertThat(auditLog.getCategory()).isEqualTo(event.category());
        assertThat(auditLog.getStatus()).isEqualTo(event.status());
        assertThat(auditLog.getAction()).isEqualTo(event.action());
        assertThat(auditLog.getEntityType()).isEqualTo(event.entityType());
        assertThat(auditLog.getEntityId()).isEqualTo(event.entityId());
        assertThat(auditLog.getPerformedById()).isEqualTo(event.performedById());
        assertThat(auditLog.getOccurredAt()).isEqualTo(event.occurredAt());
        assertThat(auditLog.getCorrelationId()).isEqualTo(UUID.fromString(event.correlationId()));
        assertThat(auditLog.getRequestId()).isEqualTo(event.requestId());
        assertThat(auditLog.getSourceService()).isEqualTo(event.sourceService());
        assertThat(auditLog.getMetadata()).isEqualTo(event.metadata());
        assertThat(auditLog.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldMapAuditLogToAuditResponse() {

        AuditLog auditLog = createAuditLog();

        AuditResponse response = auditMapper.toResponse(auditLog);

        assertThat(response).isNotNull();
        assertThat(response.eventId()).isEqualTo(auditLog.getEventId());
        assertThat(response.serviceName()).isEqualTo(auditLog.getServiceName());
        assertThat(response.category()).isEqualTo(auditLog.getCategory());
        assertThat(response.status()).isEqualTo(auditLog.getStatus());
        assertThat(response.entityType()).isEqualTo(auditLog.getEntityType());
        assertThat(response.entityId()).isEqualTo(auditLog.getEntityId());
        assertThat(response.performedById()).isEqualTo(auditLog.getPerformedById());
        assertThat(response.action()).isEqualTo(auditLog.getAction());
        assertThat(response.occurredAt()).isEqualTo(auditLog.getOccurredAt());
    }

    @Test
    void shouldThrowExceptionWhenCorrelationIdIsInvalid() {

        Instant now = Instant.now();

        AuditEvent event = AuditEvent.builder()
                .eventId(UUID.randomUUID())
                .eventVersion(1)
                .eventType(EventType.AUDIT)
                .serviceName(ServiceName.IDENTITY)
                .category(EventCategory.SECURITY)
                .action("CREATE")
                .entityType("EMPLOYEE")
                .entityId(UUID.randomUUID().toString())
                .performedById(UUID.randomUUID())
                .status(EventStatus.SUCCESS)
                .severity(EventSeverity.INFO)
                .message("Test Event")
                .occurredAt(now)
                .recordedAt(now)
                .correlationId("invalid-uuid")
                .requestId(UUID.randomUUID().toString())
                .sourceService(ServiceName.IDENTITY.name())
                .metadata(Map.of())
                .build();

        assertThatThrownBy(() -> auditMapper.toEntity(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private AuditEvent createAuditEvent() {

        Instant now = Instant.now();

        return AuditEvent.builder()
                .eventId(UUID.randomUUID())
                .eventVersion(1)
                .eventType(EventType.AUDIT)
                .serviceName(ServiceName.IDENTITY)
                .category(EventCategory.SECURITY)
                .action("CREATE")
                .entityType("EMPLOYEE")
                .entityId(UUID.randomUUID().toString())
                .performedById(UUID.randomUUID())
                .status(EventStatus.SUCCESS)
                .severity(EventSeverity.INFO)
                .message("Test Event")
                .occurredAt(now)
                .recordedAt(now)
                .correlationId(UUID.randomUUID().toString())
                .requestId(UUID.randomUUID().toString())
                .sourceService(ServiceName.IDENTITY.name())
                .metadata(new HashMap<>())
                .build();
    }

    private AuditLog createAuditLog() {

        Instant now = Instant.now();

        return AuditLog.builder()
                .eventId(UUID.randomUUID())
                .eventVersion(1)
                .eventType(EventType.AUDIT)
                .serviceName(ServiceName.IDENTITY)
                .category(EventCategory.SECURITY)
                .action("CREATE")
                .entityType("EMPLOYEE")
                .entityId(UUID.randomUUID().toString())
                .performedById(UUID.randomUUID())
                .status(EventStatus.SUCCESS)
                .severity(EventSeverity.INFO)
                .message("Test Event")
                .occurredAt(now)
                .createdAt(now)
                .correlationId(UUID.randomUUID())
                .requestId(UUID.randomUUID().toString())
                .sourceService(ServiceName.IDENTITY.name())
                .metadata(Map.of())
                .build();
    }
}