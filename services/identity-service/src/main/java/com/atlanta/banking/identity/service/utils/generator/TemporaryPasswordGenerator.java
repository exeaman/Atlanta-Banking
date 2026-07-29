package com.atlanta.banking.identity.service.utils.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TemporaryPasswordGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@#$%&*!?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final int PASSWORD_LENGTH = 12;

    public String generate() {

        List<Character> password = new ArrayList<>();

        password.add(randomChar(UPPER));
        password.add(randomChar(LOWER));
        password.add(randomChar(DIGITS));
        password.add(randomChar(SPECIAL));

        while (password.size() < PASSWORD_LENGTH) {
            password.add(randomChar(ALL));
        }

        Collections.shuffle(password, RANDOM);

        StringBuilder builder = new StringBuilder(PASSWORD_LENGTH);

        for (char c : password) {
            builder.append(c);
        }

        return builder.toString();
    }

    private char randomChar(String characters) {
        return characters.charAt(RANDOM.nextInt(characters.length()));
    }
}