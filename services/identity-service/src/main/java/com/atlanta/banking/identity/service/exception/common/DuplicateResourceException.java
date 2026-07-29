package com.atlanta.banking.identity.service.exception.common;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}