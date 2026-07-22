package com.atlanta.banking.identity.service.exception.employee;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class EmployeeAlreadyDisabledException extends BusinessException {

    public EmployeeAlreadyDisabledException(String message) {
        super(message);
    }
}