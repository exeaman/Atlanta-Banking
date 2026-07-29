package com.atlanta.banking.customer.service.exception;

public class DuplicateCustomerException extends RuntimeException{
    public DuplicateCustomerException(String message){
        super(message);
    }
}
