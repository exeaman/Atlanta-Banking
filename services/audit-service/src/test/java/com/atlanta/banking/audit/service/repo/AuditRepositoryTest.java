package com.atlanta.banking.audit.service.repo;

import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.repository.AuditRepository;
import com.atlanta.banking.audit.service.specification.AuditSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class AuditRepositoryTest {

    @Autowired
    private AuditRepository auditRepository;

    @Test
    void shouldSaveAuditLog() {

        AuditLog auditLog = createAuditLog(
                EventStatus.SUCCESS,
                ServiceName.IDENTITY
        );

        AuditLog saved = auditRepository.save(auditLog);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEventId()).isEqualTo(auditLog.getEventId());
        assertThat(saved.getStatus()).isEqualTo(EventStatus.SUCCESS);
        assertThat(saved.getServiceName()).isEqualTo(ServiceName.IDENTITY);
    }

    @Test
    void shouldFindByStatus() {

        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY));
        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.CUSTOMER));
        auditRepository.save(createAuditLog(EventStatus.FAILURE, ServiceName.IDENTITY));

        AuditSearchCriteria criteria = new AuditSearchCriteria(

                null, EventStatus.SUCCESS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<AuditLog> result = auditRepository.findAll(
                AuditSpecification.from(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(AuditLog::getStatus)
                .containsOnly(EventStatus.SUCCESS);
    }

    @Test
    void shouldReturnPagedResults() {

        for (int i = 0; i < 10; i++) {
            auditRepository.save(
                    createAuditLog(
                            EventStatus.SUCCESS,
                            ServiceName.IDENTITY
                    )
            );
        }

        Page<AuditLog> page = auditRepository.findAll(PageRequest.of(0, 5));

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void shouldFindByStatusAndServiceName() {

        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY));
        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.CUSTOMER));
        auditRepository.save(createAuditLog(EventStatus.FAILURE, ServiceName.IDENTITY));

        AuditSearchCriteria criteria = new AuditSearchCriteria(
                ServiceName.IDENTITY,
                EventStatus.SUCCESS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<AuditLog> result = auditRepository.findAll(
                AuditSpecification.from(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);

        AuditLog auditLog = result.getContent().getFirst();

        assertThat(auditLog.getStatus()).isEqualTo(EventStatus.SUCCESS);
        assertThat(auditLog.getServiceName()).isEqualTo(ServiceName.IDENTITY);
    }

    @Test
    void shouldReturnEmptyWhenNoMatchFound() {

        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY));

        AuditSearchCriteria criteria = new AuditSearchCriteria(
                ServiceName.CUSTOMER,
                EventStatus.FAILURE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<AuditLog> result = auditRepository.findAll(
                AuditSpecification.from(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnResultsSortedByOccurredAt() {

        AuditLog older = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        older.setOccurredAt(Instant.now().minusSeconds(60));

        AuditLog newer = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        newer.setOccurredAt(Instant.now());

        auditRepository.save(older);
        auditRepository.save(newer);

        Page<AuditLog> page = auditRepository.findAll(
                PageRequest.of(0, 10,
                        org.springframework.data.domain.Sort.by("occurredAt").descending())
        );

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getOccurredAt())
                .isAfter(page.getContent().get(1).getOccurredAt());
    }

    @Test
    void shouldRejectDuplicateEventId() {

        UUID eventId = UUID.randomUUID();

        AuditLog first = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        first.setEventId(eventId);

        AuditLog second = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        second.setEventId(eventId);

        auditRepository.saveAndFlush(first);

        assertThatThrownBy(() -> auditRepository.saveAndFlush(second)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnAllRecordsWhenSearchCriteriaIsEmpty() {

        auditRepository.save(createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY));
        auditRepository.save(createAuditLog(EventStatus.FAILURE, ServiceName.CUSTOMER));
        auditRepository.save(createAuditLog(EventStatus.PARTIAL_SUCCESS, ServiceName.ACCOUNTS));

        AuditSearchCriteria criteria = new AuditSearchCriteria(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<AuditLog> result = auditRepository.findAll(
                AuditSpecification.from(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void shouldFindEventsOccurredAfterGivenInstant() {

        Instant cutoff = Instant.now();

        AuditLog oldLog = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        oldLog.setOccurredAt(cutoff.minusSeconds(60));

        AuditLog newLog = createAuditLog(EventStatus.SUCCESS, ServiceName.IDENTITY);
        newLog.setOccurredAt(cutoff.plusSeconds(60));

        auditRepository.save(oldLog);
        auditRepository.save(newLog);

        AuditSearchCriteria criteria = new AuditSearchCriteria(
                null,
                null,
                null,
                null,
                null,
                null,
                cutoff,
                null
        );

        Page<AuditLog> result = auditRepository.findAll(
                AuditSpecification.from(criteria),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent())
                .hasSize(1)
                .first()
                .extracting(AuditLog::getOccurredAt)
                .isEqualTo(newLog.getOccurredAt());
    }

    @Test
    void shouldReturnTrueWhenEventIdExists() {

        AuditLog auditLog = createAuditLog(
                EventStatus.SUCCESS,
                ServiceName.IDENTITY
        );

        auditRepository.save(auditLog);

        assertThat(
                auditRepository.existsByEventId(auditLog.getEventId())
        ).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEventIdDoesNotExist() {

        assertThat(
                auditRepository.existsByEventId(UUID.randomUUID())
        ).isFalse();
    }

    private AuditLog createAuditLog(
            EventStatus status,
            ServiceName serviceName) {

        Instant now = Instant.now();

        return AuditLog.builder()
                .metadata(new HashMap<>())
                .eventId(UUID.randomUUID())
                .eventVersion(1)
                .eventType(EventType.AUDIT)
                .serviceName(serviceName)
                .category(EventCategory.SECURITY)
                .action("CREATE")
                .entityType("EMPLOYEE")
                .entityId(UUID.randomUUID().toString())
                .performedById(UUID.randomUUID())
                .status(status)
                .severity(EventSeverity.INFO)
                .message("Test audit event")
                .occurredAt(now)
                .createdAt(now)
                .correlationId(UUID.randomUUID())
                .requestId(UUID.randomUUID().toString())
                .sourceService(serviceName.name())
                .build();
    }
}