package com.atlanta.banking.identity.service.exception.role;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class UnauthorizedRoleAssignmentException extends BusinessException {

    public UnauthorizedRoleAssignmentException(String message) {
        super(message);
    }
}