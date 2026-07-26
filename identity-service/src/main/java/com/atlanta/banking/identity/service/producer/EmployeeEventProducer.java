package com.atlanta.banking.identity.service.producer;

import com.atlanta.banking.identity.service.event.EmployeeCreatedEvent;
import com.atlanta.banking.identity.service.topics.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;

    public void publishEmployeeCreated(EmployeeCreatedEvent event) {

        kafkaTemplate.send(
                KafkaTopics.IDENTITY_EMPLOYEE,
                event.employeeId(),
                event
        );
    }
}