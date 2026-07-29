package com.atlanta.banking.customer.service.exception;

public class UnderAgeCustomerException extends RuntimeException{

    public UnderAgeCustomerException(String message){
        super(message);
    }

}
