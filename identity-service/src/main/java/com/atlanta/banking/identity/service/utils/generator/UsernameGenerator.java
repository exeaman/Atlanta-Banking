package com.atlanta.banking.identity.service.utils.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;

@Component
public class UsernameGenerator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String firstTwo(String name) {

        String cleaned = name.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);

        if (cleaned.length() >= 2) {
            return cleaned.substring(0, 2);
        }

        return ("XX" + cleaned)
                .substring(("XX" + cleaned).length() - 2);
    }

    private String lastTwo(String name) {

        String cleaned = name.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);

        if (cleaned.length() >= 2) {
            return cleaned.substring(cleaned.length() - 2);
        }

        return ("XX" + cleaned)
                .substring(("XX" + cleaned).length() - 2);
    }

    public String generate(String firstName, String lastName) {

        String prefix = firstTwo(firstName);
        String suffix = lastTwo(lastName);

        StringBuilder random = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            random.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        return prefix + random + suffix;
    }
}