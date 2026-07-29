package com.atlanta.banking.audit.service.services;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.event.EmployeeCreatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditService {

    void recordEvent(AuditEvent event);

    Page<AuditResponse> getAuditEvents(Pageable pageable);

    Page<AuditResponse> getAuditEvents(
            AuditSearchCriteria criteria,
            Pageable pageable
    );

    void processEmployeeCreated(EmployeeCreatedEvent event);

}
