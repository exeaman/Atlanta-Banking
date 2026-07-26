package com.atlanta.banking.audit.service.entity;

import com.atlanta.banking.audit.service.contract.audit.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "audit_log",
        indexes = {
                @Index(name = "idx_audit_occurred_at", columnList = "occurred_at"),
                @Index(name = "idx_audit_service", columnList = "service_name"),
                @Index(name = "idx_audit_action", columnList = "action"),
                @Index(name = "idx_audit_entity", columnList = "entity_id"),
                @Index(name = "idx_audit_actor", columnList = "performed_by_id"),
                @Index(name = "idx_audit_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(updatable = false)
    Map<String, Object> metadata;
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID eventId;
    @Column(nullable = false, updatable = false)
    private int eventVersion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventType eventType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ServiceName serviceName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventCategory category;
    @Column(nullable = false, updatable = false)
    private String action;
    @Column(nullable = false, updatable = false)
    private String entityType;
    @Column(nullable = false, updatable = false)
    private String entityId;
    @Column(updatable = false)
    private UUID performedById;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EventSeverity severity;
    @Column(length = 2000, updatable = false)
    private String message;
    @Column(nullable = false, updatable = false)
    private Instant occurredAt;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column(updatable = false)
    private UUID correlationId;
    @Column(updatable = false)
    private String requestId;
    @Column(nullable = false, updatable = false)
    private String sourceService;
}