package com.atlanta.banking.customer.service.dto;

import lombok.Data;


@Data
public class CustomExceptionResponseDto {
    private String exceptionName;
    private String date;
    private String desc;
}
