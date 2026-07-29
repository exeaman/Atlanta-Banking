package com.atlanta.banking.audit.service.specification;

import com.atlanta.banking.audit.service.contract.audit.enums.EventCategory;
import com.atlanta.banking.audit.service.contract.audit.enums.EventStatus;
import com.atlanta.banking.audit.service.contract.audit.enums.ServiceName;
import com.atlanta.banking.audit.service.dto.AuditSearchCriteria;
import com.atlanta.banking.audit.service.entity.AuditLog;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public final class AuditSpecification {

    private AuditSpecification() {
    }

    public static Specification<AuditLog> from(
            AuditSearchCriteria criteria) {
        Specification<AuditLog> spec = Specification.allOf();

        if (criteria.status() != null)
            spec = spec.and(hasStatus(criteria.status()));

        if (criteria.serviceName() != null)
            spec = spec.and(hasServiceName(criteria.serviceName()));

        if (criteria.category() != null)
            spec = spec.and(hasCategory(criteria.category()));

        if (criteria.performedById() != null)
            spec = spec.and(hasPerformedById(criteria.performedById()));

        if (criteria.entityType() != null)
            spec = spec.and(hasEntityType(criteria.entityType()));

        if (criteria.entityId() != null)
            spec = spec.and(hasEntityId(criteria.entityId()));

        if (criteria.occurredAfter() != null)
            spec = spec.and(occurredAtOnOrAfter(criteria.occurredAfter()));

        if (criteria.occurredBefore() != null)
            spec = spec.and(occurredAtOnOrBefore(criteria.occurredBefore()));

        return spec;

    }

    private static Specification<AuditLog> hasStatus(
            EventStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    private static Specification<AuditLog> hasServiceName(
            ServiceName serviceName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("serviceName"),
                        serviceName
                );
    }

    private static Specification<AuditLog> hasCategory(
            EventCategory category) {

        return (root, query, cb) ->
                cb.equal(root.get("category"), category);
    }

    private static Specification<AuditLog> hasPerformedById(
            UUID performedById) {

        return (root, query, cb) ->
                cb.equal(root.get("performedById"), performedById);
    }

    private static Specification<AuditLog> hasEntityType(
            String entityType) {

        return (root, query, cb) ->
                cb.equal(root.get("entityType"), entityType);
    }

    private static Specification<AuditLog> hasEntityId(
            String entityId) {

        return (root, query, cb) ->
                cb.equal(root.get("entityId"), entityId);
    }

    private static Specification<AuditLog> occurredAtOnOrAfter(
            Instant occurredAfter) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("occurredAt"),
                        occurredAfter
                );
    }

    private static Specification<AuditLog> occurredAtOnOrBefore(
            Instant occurredBefore) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("occurredAt"),
                        occurredBefore
                );
    }
}