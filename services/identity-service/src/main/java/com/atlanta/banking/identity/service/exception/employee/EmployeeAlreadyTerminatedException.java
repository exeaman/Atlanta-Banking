package com.atlanta.banking.identity.service.exception.employee;

import com.atlanta.banking.identity.service.exception.BusinessException;

public class EmployeeAlreadyTerminatedException extends BusinessException {

    public EmployeeAlreadyTerminatedException(String message) {
        super(message);
    }
}