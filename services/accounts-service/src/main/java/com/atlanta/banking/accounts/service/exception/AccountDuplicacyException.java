package com.atlanta.banking.accounts.service.exception;

public class AccountDuplicacyException extends RuntimeException {
    public AccountDuplicacyException(String message) {
        super(message);
    }
}
