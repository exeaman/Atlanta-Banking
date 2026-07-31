package com.atlanta.banking.identity.service.security;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            "VGhpc0lzQVN1cGVyU2VjcmV0S2V5Rm9ySldUU2lnbmluZzEyMzQ1Njc4OTA=";

    private JwtService jwtService;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 60_000L);
        userDetails = createUser("john");
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

        CustomUserDetails otherUser = createUser("alex");

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

    private CustomUserDetails createUser(String username) {

        Role role = Role.builder()
                .name("ROLE_ADMIN")
                .build();

        Employee employee = Employee.builder()
                .username(username)
                .password("password")
                .enabled(true)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .roles(Set.of(role))
                .build();

        return new CustomUserDetails(employee);
    }
}