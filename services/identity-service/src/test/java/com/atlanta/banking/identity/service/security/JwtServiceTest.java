package com.atlanta.banking.identity.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private static final String SECRET =
            "VGhpc0lzQVN1cGVyU2VjcmV0S2V5Rm9ySldUU2lnbmluZzEyMzQ1Njc4OTA=";

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 60_000L);

        userDetails = User.withUsername("john")
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        assertEquals("john", jwtService.extractUsername(token));
    }

    @Test
    void shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = User.withUsername("alex")
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void shouldExtractExpiration() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);

        long diff = expiration.getTime() - System.currentTimeMillis();

        assertTrue(diff > 59_000 && diff <= 60_000);
    }

    @Test
    void shouldExtractIssuedAt() {
        String token = jwtService.generateToken(userDetails);

        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);

        assertNotNull(issuedAt);
    }

    @Test
    void shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtService shortLivedJwt = new JwtService(SECRET, 1L);

        String token = shortLivedJwt.generateToken(userDetails);

        Thread.sleep(5);

        assertThrows(
                ExpiredJwtException.class,
                () -> shortLivedJwt.isTokenValid(token, userDetails)
        );
    }

    @Test
    void shouldThrowExceptionForMalformedToken() {
        assertThrows(
                JwtException.class,
                () -> jwtService.extractUsername("abc.def")
        );
    }

    @Test
    void shouldThrowExceptionForInvalidSignature() {
        JwtService anotherJwt = new JwtService(
                "QW5vdGhlclN1cGVyU2VjcmV0S2V5MTIzNDU2Nzg5MDEyMzQ1Ng==",
                60_000L
        );

        String token = anotherJwt.generateToken(userDetails);

        assertThrows(
                JwtException.class,
                () -> jwtService.extractUsername(token)
        );
    }

    @Test
    void shouldThrowExceptionForEmptyToken() {
        assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.extractUsername("")
        );
    }
}