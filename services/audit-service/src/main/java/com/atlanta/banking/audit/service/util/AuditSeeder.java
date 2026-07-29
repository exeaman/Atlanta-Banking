package com.atlanta.banking.audit.service.util;

import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class AuditSeeder implements CommandLineRunner {

    private final AuditRepository auditRepository;

    @Override
    public void run(String... args) {

        if (auditRepository.count() >= 30) {
            log.info("Audit database already contains seeded records. Skipping.");
            return;
        }

        log.info("Seeding audit log data...");

        Instant baseTime = Instant.now().minus(7, ChronoUnit.DAYS);

        UUID superAdmin =
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        UUID teller =
                UUID.fromString("3f829a10-2b4c-4e89-8d10-1234567890ab");

        UUID manager =
                UUID.fromString("88a10b92-4f11-4d1a-a223-998877665544");

        UUID system =
                UUID.fromString("00000000-0000-0000-0000-000000000000");

        Map<ServiceName, String[]> actions = Map.of(
                ServiceName.IDENTITY,
                new String[]{"EMPLOYEE_LOGIN", "CHANGE_PASSWORD", "DISABLE_EMPLOYEE", "ASSIGN_ROLE"},

                ServiceName.CUSTOMER,
                new String[]{"CREATE_CUSTOMER", "UPDATE_CUSTOMER", "VERIFY_KYC"},

                ServiceName.ACCOUNTS,
                new String[]{"OPEN_ACCOUNT", "FREEZE_ACCOUNT", "CLOSE_ACCOUNT"},

                ServiceName.PAYMENTS,
                new String[]{"NEFT_TRANSFER", "IMPS_TRANSFER", "REFUND_PAYMENT"},

                ServiceName.LEDGER,
                new String[]{"POST_ENTRY", "BALANCE_RECALCULATION"},

                ServiceName.NOTIFICATION,
                new String[]{"SEND_SMS", "SEND_EMAIL"},

                ServiceName.AUDIT,
                new String[]{"EXPORT_LOGS", "PURGE_ARCHIVE"}
        );

        List<AuditLog> logs = new ArrayList<>();

        ServiceName[] services = ServiceName.values();
        EventCategory[] categories = EventCategory.values();

        for (int i = 1; i <= 35; i++) {

            ServiceName service = services[i % services.length];

            String action =
                    actions.get(service)[i % actions.get(service).length];

            EventStatus status =
                    i % 7 == 0
                            ? EventStatus.FAILURE
                            : i % 13 == 0
                            ? EventStatus.PARTIAL_SUCCESS
                            : EventStatus.SUCCESS;

            EventSeverity severity =
                    switch (status) {
                        case FAILURE -> i % 2 == 0
                                ? EventSeverity.HIGH
                                : EventSeverity.CRITICAL;

                        case PARTIAL_SUCCESS -> EventSeverity.MEDIUM;

                        default -> EventSeverity.INFO;
                    };

            UUID actor;

            if (i % 4 == 0) {
                actor = system;
            } else if (i % 3 == 0) {
                actor = teller;
            } else if (i % 5 == 0) {
                actor = manager;
            } else {
                actor = superAdmin;
            }

            Instant occurredAt =
                    baseTime.plus(i * 4L, ChronoUnit.HOURS);

            Map<String, Object> metadata = new HashMap<>();

            metadata.put("environment", "development");
            metadata.put("clientIp", "192.168.1." + (20 + i));
            metadata.put("node", "identity-node-" + ((i % 3) + 1));
            metadata.put("iteration", i);

            if (status == EventStatus.FAILURE) {
                metadata.put("errorCode", "ERR-" + (400 + i));
                metadata.put("reason", "Simulated failure");
            }

            AuditLog logEntry = AuditLog.builder()
                    .eventId(UUID.randomUUID())
                    .eventVersion(1)
                    .eventType(EventType.AUDIT)
                    .serviceName(service)
                    .category(categories[i % categories.length])
                    .action(action)
                    .entityType(service.name() + "_ENTITY")
                    .entityId("ENT-" + (100000 + i))
                    .performedById(actor)
                    .status(status)
                    .severity(severity)
                    .message(action + " executed with status " + status)
                    .occurredAt(occurredAt)
                    .createdAt(occurredAt.plusMillis(10))
                    .correlationId(UUID.randomUUID())
                    .requestId("REQ-" + UUID.randomUUID().toString().substring(0, 8))
                    .sourceService(service.name().toLowerCase() + "-service")
                    .metadata(metadata)
                    .build();

            logs.add(logEntry);
        }

        auditRepository.saveAll(logs);

        log.info("Seeded {} audit records.", logs.size());
    }
}