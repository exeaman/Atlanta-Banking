package com.atlanta.banking.identity.service.DummyTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password123"));
    }
}

// user name  is "aman"
//$2a$10$Qx5ZE7mNeHpd25A5ezhaO.7o6YVbLQF7PgTVaVUz/Wfqk9z4NNeRq