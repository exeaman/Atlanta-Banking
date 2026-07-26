package com.atlanta.banking.audit.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class AuditServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(AuditServiceApplication.class, args);
    }

}
