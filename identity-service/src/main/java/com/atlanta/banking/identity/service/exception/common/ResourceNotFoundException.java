package com.atlanta.banking.identity.service.exception.common;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}