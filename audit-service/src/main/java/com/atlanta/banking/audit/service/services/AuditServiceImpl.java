package com.atlanta.banking.audit.service.services;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.exception.DuplicateAuditEventException;
import com.atlanta.banking.audit.service.mapper.AuditMapper;
import com.atlanta.banking.audit.service.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private static final Logger log =
            LoggerFactory.getLogger(AuditServiceImpl.class);

    @Override
    public void recordEvent(AuditEvent event) {
        log.debug(
                "Recording audit event [{}] from service [{}]",
                event.eventId(),
                event.serviceName()
        );
        if (auditRepository.existsByEventId(event.eventId())) {
            throw new DuplicateAuditEventException(
                    "Audit event already exists: " + event.eventId()
            );
        }

        AuditLog auditLog = auditMapper.toEntity(event);

        auditRepository.save(auditLog);
    }
}