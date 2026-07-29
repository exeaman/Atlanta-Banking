package com.atlanta.banking.accounts.service.dto;

import lombok.Data;

@Data
public class AccountExceptionDto {
    private String message;
    private String time;
    private String date;
    private String exception;
    private String code;
}
