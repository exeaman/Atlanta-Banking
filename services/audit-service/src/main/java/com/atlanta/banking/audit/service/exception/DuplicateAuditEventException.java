package com.atlanta.banking.audit.service.exception;

public class DuplicateAuditEventException extends RuntimeException {

    public DuplicateAuditEventException(String message) {
        super(message);
    }
}