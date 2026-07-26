package com.atlanta.banking.audit.service.controller;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.contract.audit.enums.*;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.security.JwtAuthenticationFilter;
import com.atlanta.banking.audit.service.security.JwtService;
import com.atlanta.banking.audit.service.services.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)

@AutoConfigureMockMvc(addFilters = false)
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuditService auditService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldRecordAuditEvent() throws Exception {

        AuditEvent event = createAuditEvent();

        mockMvc.perform(post("/api/v1/audit/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isNoContent());

        verify(auditService).recordEvent(any(AuditEvent.class));
        verifyNoMoreInteractions(auditService);
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {

        AuditEvent event = AuditEvent.builder().build();

        mockMvc.perform(post("/api/v1/audit/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(auditService);
    }

    @Test
    void shouldReturnPagedAuditEvents() throws Exception {

        AuditResponse response = createAuditResponse();

        Page<AuditResponse> page =
                new PageImpl<>(List.of(response));

        when(auditService.getAuditEvents(
                any(AuditSearchCriteria.class),
                any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/audit/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].action")
                        .value(response.action()));

        verify(auditService)
                .getAuditEvents(any(AuditSearchCriteria.class),
                        any(Pageable.class));

        verifyNoMoreInteractions(auditService);
    }

    @Test
    void shouldReturnEmptyPageWhenNoAuditEventsExist() throws Exception {

        when(auditService.getAuditEvents(
                any(AuditSearchCriteria.class),
                any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/audit/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(auditService)
                .getAuditEvents(any(AuditSearchCriteria.class),
                        any(Pageable.class));

        verifyNoMoreInteractions(auditService);
    }

    @Test
    void shouldReturnFilteredAuditEvents() throws Exception {

        AuditResponse response = createAuditResponse();

        when(auditService.getAuditEvents(
                any(AuditSearchCriteria.class),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/v1/audit/events")
                        .param("serviceName", "IDENTITY")
                        .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].serviceName")
                        .value("IDENTITY"));

        verify(auditService)
                .getAuditEvents(any(AuditSearchCriteria.class),
                        any(Pageable.class));

        verifyNoMoreInteractions(auditService);
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