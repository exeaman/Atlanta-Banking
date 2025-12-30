package com.atlanta.banking.customer.service.exception;

public class InvalidCustomerStateException extends RuntimeException{
    public InvalidCustomerStateException(String message){
        super(message);
    }
}
