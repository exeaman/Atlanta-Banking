package com.atlanta.banking.identity.service.kafka;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.mapper.EmployeeEventMapper;
import com.atlanta.common.events.constants.ServiceName;
import com.atlanta.common.events.employee.EmployeeCreatedEvent;
import com.atlanta.common.events.employee.EmployeeEventType;
import com.atlanta.common.events.metadata.EventMetadata;
import com.atlanta.common.events.topics.EmployeeTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeEventPublisher {

    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;
    private final EmployeeEventMapper employeeEventMapper;

    public void publishEmployeeCreated(Employee employee) {

        EventMetadata metadata = new EventMetadata(
                UUID.randomUUID(),
                null,
                Instant.now(),
                ServiceName.IDENTITY,
                EmployeeEventType.EMPLOYEE_CREATED.name(),
                1
        );

        EmployeeCreatedEvent event =
                employeeEventMapper.toEmployeeCreatedEvent(employee, metadata);

        kafkaTemplate.send(EmployeeTopics.CREATED, event);
    }
}