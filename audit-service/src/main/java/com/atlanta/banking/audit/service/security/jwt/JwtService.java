package com.atlanta.banking.audit.service.security.jwt;

import com.atlanta.banking.audit.service.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey signingKey;

    public String extractEmployeeId(String token) {
        log.info("Extracting EmpID");
        return extractAllClaims(token).getSubject();
    }

    public String extractUsername(String token) {
        log.info("Extracting username");
        return extractAllClaims(token).get("username", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            log.info("Issuer: {}", claims.getIssuer());
            log.info("Audience: {}", claims.getAudience());
            return SecurityConstants.ISSUER.equals(claims.getIssuer())
                    && claims.getAudience().contains(SecurityConstants.AUDIENCE);
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("JWT validation failed", ex);
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}