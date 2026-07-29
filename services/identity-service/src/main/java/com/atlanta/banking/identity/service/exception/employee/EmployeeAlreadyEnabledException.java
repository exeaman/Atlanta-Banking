package com.atlanta.banking.identity.service.exception.employee;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class EmployeeAlreadyEnabledException extends BusinessException {

    public EmployeeAlreadyEnabledException(String message) {
        super(message);
    }
}