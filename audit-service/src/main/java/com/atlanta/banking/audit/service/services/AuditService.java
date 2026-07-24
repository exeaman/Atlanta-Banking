package com.atlanta.banking.audit.service.services;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;

public interface AuditService {

    void recordEvent(AuditEvent event);

}
