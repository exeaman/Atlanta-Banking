package com.atlanta.banking.audit.service.consumer;

import com.atlanta.banking.audit.service.event.EmployeeCreatedEvent;
import com.atlanta.banking.audit.service.services.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventConsumer {

    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    @KafkaListener(
            topics = KafkaTopics.IDENTITY_EMPLOYEE,
            groupId = "audit-service"
    )
    public void consume(Map<String, Object> payload) {

        EmployeeCreatedEvent event =
                objectMapper.convertValue(payload, EmployeeCreatedEvent.class);

        auditService.processEmployeeCreated(event);
    }
}