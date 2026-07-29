package com.atlanta.banking.audit.service.services;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.exception.DuplicateAuditEventException;
import com.atlanta.banking.audit.service.mapper.AuditMapper;
import com.atlanta.banking.audit.service.repository.AuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditServiceImplTest {

    @InjectMocks
    private AuditServiceImpl auditService;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditMapper auditMapper;

    @Test
    void shouldRecordAuditEvent() {

        AuditEvent event = createAuditEvent();
        AuditLog auditLog = createAuditLog();

        when(auditRepository.existsByEventId(event.eventId()))
                .thenReturn(false);

        when(auditMapper.toEntity(event))
                .thenReturn(auditLog);

        auditService.recordEvent(event);

        verify(auditRepository).existsByEventId(event.eventId());
        verify(auditMapper).toEntity(event);
        verify(auditRepository).save(auditLog);

        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldThrowDuplicateAuditEventExceptionWhenEventAlreadyExists() {

        AuditEvent event = createAuditEvent();

        when(auditRepository.existsByEventId(event.eventId()))
                .thenReturn(true);

        assertThatThrownBy(() -> auditService.recordEvent(event))
                .isInstanceOf(DuplicateAuditEventException.class)
                .hasMessageContaining(event.eventId().toString());

        verify(auditRepository).existsByEventId(event.eventId());

        verify(auditMapper, never()).toEntity(any());
        verify(auditRepository, never()).save(any());

        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldReturnPagedAuditEvents() {

        Pageable pageable = PageRequest.of(0, 10);

        AuditLog auditLog = createAuditLog();
        AuditResponse response = createAuditResponse();

        when(auditRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(auditLog), pageable, 1));

        when(auditMapper.toResponse(auditLog))
                .thenReturn(response);

        Page<AuditResponse> result = auditService.getAuditEvents(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);

        verify(auditRepository).findAll(pageable);
        verify(auditMapper).toResponse(auditLog);
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldReturnFilteredAuditEvents() {

        Pageable pageable = PageRequest.of(0, 10);

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

        AuditLog auditLog = createAuditLog();
        AuditResponse response = createAuditResponse();

        when(auditRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(auditLog), pageable, 1));

        when(auditMapper.toResponse(auditLog))
                .thenReturn(response);

        Page<AuditResponse> result =
                auditService.getAuditEvents(criteria, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);

        verify(auditRepository).findAll(
                ArgumentMatchers.<Specification<AuditLog>>any(),
                eq(pageable)
        );
        verify(auditMapper).toResponse(auditLog);
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldReturnEmptyPageWhenNoAuditEventsExist() {

        Pageable pageable = PageRequest.of(0, 10);

        when(auditRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        Page<AuditResponse> result =
                auditService.getAuditEvents(pageable);

        assertThat(result).isEmpty();

        verify(auditRepository).findAll(pageable);
        verify(auditMapper, never()).toResponse(any());
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldReturnEmptyPageWhenFilteredSearchFindsNothing() {

        Pageable pageable = PageRequest.of(0, 10);

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

        when(auditRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty(pageable));

        Page<AuditResponse> result =
                auditService.getAuditEvents(criteria, pageable);

        assertThat(result).isEmpty();

        verify(auditRepository).findAll(
                ArgumentMatchers.<Specification<AuditLog>>any(),
                eq(pageable)
        );
        verify(auditMapper, never()).toResponse(any());
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldPropagateMapperException() {

        AuditEvent event = createAuditEvent();

        when(auditRepository.existsByEventId(event.eventId()))
                .thenReturn(false);

        when(auditMapper.toEntity(event))
                .thenThrow(new IllegalArgumentException("Mapping failed"));

        assertThatThrownBy(() -> auditService.recordEvent(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mapping failed");

        verify(auditRepository).existsByEventId(event.eventId());
        verify(auditMapper).toEntity(event);
        verify(auditRepository, never()).save(any());
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }

    @Test
    void shouldPropagateRepositoryException() {

        AuditEvent event = createAuditEvent();
        AuditLog auditLog = createAuditLog();

        when(auditRepository.existsByEventId(event.eventId()))
                .thenReturn(false);

        when(auditMapper.toEntity(event))
                .thenReturn(auditLog);

        when(auditRepository.save(auditLog))
                .thenThrow(new RuntimeException("Database unavailable"));

        assertThatThrownBy(() -> auditService.recordEvent(event))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database unavailable");

        verify(auditRepository).existsByEventId(event.eventId());
        verify(auditMapper).toEntity(event);
        verify(auditRepository).save(auditLog);
        verifyNoMoreInteractions(auditRepository, auditMapper);
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
                .message("Test audit event")
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
                .message("Test audit event")
                .occurredAt(now)
                .createdAt(now)
                .correlationId(UUID.randomUUID())
                .requestId(UUID.randomUUID().toString())
                .sourceService(ServiceName.IDENTITY.name())
                .metadata(new HashMap<>())
                .build();
    }

    private AuditResponse createAuditResponse() {

        return new AuditResponse(
                UUID.randomUUID(),
                ServiceName.IDENTITY,
                EventCategory.SECURITY,
                EventStatus.SUCCESS,
                "EMPLOYEE",
                UUID.randomUUID().toString(),
                UUID.randomUUID(),
                "CREATE",
                Instant.now()
        );
    }
}