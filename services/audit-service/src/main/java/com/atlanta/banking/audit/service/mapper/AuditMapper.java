package com.atlanta.banking.audit.service.mapper;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.event.EmployeeCreatedEvent;

public interface AuditMapper {

    AuditLog toEntity(AuditEvent event);

    AuditResponse toResponse(AuditLog auditLog);

    AuditEvent toEvent(EmployeeCreatedEvent event);
}
