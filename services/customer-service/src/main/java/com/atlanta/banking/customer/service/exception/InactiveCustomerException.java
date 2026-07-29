package com.atlanta.banking.customer.service.exception;

public class InactiveCustomerException extends RuntimeException{
    public InactiveCustomerException(String message){
        super(message);
    }
}
