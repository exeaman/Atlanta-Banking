package com.atlanta.banking.accounts.service.exception;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.atlanta.banking.accounts.service.dto.AccountExceptionDto;

import io.swagger.v3.oas.annotations.Hidden;


@RestControllerAdvice (basePackages = "com.atlanta.banking")
@Hidden
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountDuplicacyException.class)
    public ResponseEntity<AccountExceptionDto> accountDuplicacyException(AccountDuplicacyException e) {
        AccountExceptionDto dto = new AccountExceptionDto();
        dto.setCode(HttpStatus.BAD_REQUEST.toString());
        dto.setDate(LocalDate.now().toString());
        dto.setTime(LocalTime.now().toString());
        dto.setMessage(e.getMessage());
        dto.setException("Account Duplicacy Exception");
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<AccountExceptionDto> accountNotFoundException(AccountNotFoundException e) {
        AccountExceptionDto dto = new AccountExceptionDto();
        dto.setCode(HttpStatus.NOT_FOUND.toString());
        dto.setDate(LocalDate.now().toString());
        dto.setTime(LocalTime.now().toString());
        dto.setMessage(e.getMessage());
        dto.setException("Account Not Found Exception");
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    public ResponseEntity<AccountExceptionDto> invalidAccountStateException(InvalidAccountStateException e) {
        AccountExceptionDto dto = new AccountExceptionDto();
        dto.setCode(HttpStatus.CONFLICT.toString());
        dto.setDate(LocalDate.now().toString());
        dto.setTime(LocalTime.now().toString());
        dto.setMessage(e.getMessage());
        dto.setException("Invalid Account State Exception");
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidCustomerException.class)
    public ResponseEntity<AccountExceptionDto> invalidAccountStateException(InvalidCustomerException e) {
        AccountExceptionDto dto = new AccountExceptionDto();
        dto.setCode(HttpStatus.CONFLICT.toString());
        dto.setDate(LocalDate.now().toString());
        dto.setTime(LocalTime.now().toString());
        dto.setMessage(e.getMessage());
        dto.setException("Invalid Customer Exception");
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AccountExceptionDto> handleGenericException(Exception e) {
        AccountExceptionDto dto = new AccountExceptionDto();
        dto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        dto.setDate(LocalDate.now().toString());
        dto.setTime(LocalTime.now().toString());
        dto.setMessage(e.getMessage());
        dto.setException("Internal Server Error");
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
