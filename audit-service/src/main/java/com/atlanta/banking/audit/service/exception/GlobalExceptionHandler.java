package com.atlanta.banking.audit.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAuditEventException.class)
    public ProblemDetail handleDuplicateAuditEvent(
            DuplicateAuditEventException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate Audit Event");
        problem.setDetail(ex.getMessage());

        return problem;
    }
}