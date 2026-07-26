package com.atlanta.banking.audit.service.services;

import com.atlanta.banking.audit.service.contract.audit.AuditEvent;
import com.atlanta.banking.audit.service.dto.AuditResponse;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.entity.AuditLog;
import com.atlanta.banking.audit.service.exception.DuplicateAuditEventException;
import com.atlanta.banking.audit.service.mapper.AuditMapper;
import com.atlanta.banking.audit.service.repository.AuditRepository;
import com.atlanta.banking.audit.service.specification.AuditSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private static final Logger log =
            LoggerFactory.getLogger(AuditServiceImpl.class);
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

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

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> getAuditEvents(Pageable pageable) {
        return auditRepository.findAll(pageable).map(auditMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> getAuditEvents(
            AuditSearchCriteria criteria,
            Pageable pageable) {

        return auditRepository
                .findAll(AuditSpecification.from(criteria), pageable)
                .map(auditMapper::toResponse);
    }
}