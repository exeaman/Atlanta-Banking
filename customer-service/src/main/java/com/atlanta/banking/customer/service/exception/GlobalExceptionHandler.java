
package com.atlanta.banking.customer.service.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.atlanta.banking.customer.service.dto.CustomExceptionResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<CustomExceptionResponseDto> customerNotFoundHandler(CustomerNotFoundException e) {
        CustomExceptionResponseDto ex = new CustomExceptionResponseDto();
        ex.setDate(Instant.now().toString());
        ex.setDesc(e.getMessage());
        ex.setExceptionName("CustomerNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
    }

    @ExceptionHandler(InvalidCustomerStateException.class)
    public ResponseEntity<CustomExceptionResponseDto> invalidCustomerStateHandler(InvalidCustomerStateException e) {
        CustomExceptionResponseDto ex = new CustomExceptionResponseDto();
        ex.setDate(Instant.now().toString());
        ex.setDesc(e.getMessage());
        ex.setExceptionName("InvalidCustomerStateException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(InactiveCustomerException.class)
    public ResponseEntity<CustomExceptionResponseDto> inactiveCustomerHandler(InactiveCustomerException e) {
        CustomExceptionResponseDto ex = new CustomExceptionResponseDto();
        ex.setDate(Instant.now().toString());
        ex.setDesc(e.getMessage());
        ex.setExceptionName("InactiveCustomerException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<CustomExceptionResponseDto> duplicateCustomerExceptionHandler(DuplicateCustomerException e) {
        CustomExceptionResponseDto ex = new CustomExceptionResponseDto();
        ex.setDate(Instant.now().toString());
        ex.setDesc(e.getMessage());
        ex.setExceptionName("DuplicateCustomerException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponseDto> genericExceptionHandler(Exception e) {
        CustomExceptionResponseDto ex = new CustomExceptionResponseDto();
        ex.setDate(Instant.now().toString());
        ex.setDesc(e.getMessage());
        ex.setExceptionName(e.getMessage());
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(ex);
    }

    
}