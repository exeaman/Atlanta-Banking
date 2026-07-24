package com.atlanta.banking.identity.service.exception.employee;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
