package com.atlanta.banking.audit.service.security.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Bean
    SecretKey signingKey(@Value("${jwt.secret}") String secret) {

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}