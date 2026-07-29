package com.atlanta.banking.identity.service.exception.role;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class InvalidRoleAssignmentException extends BusinessException {

    public InvalidRoleAssignmentException(String message) {
        super(message);
    }
}