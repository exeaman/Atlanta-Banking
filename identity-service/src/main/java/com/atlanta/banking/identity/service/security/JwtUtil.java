package com.atlanta.banking.identity.service.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ Recommended: store a BASE64 secret in env/config, not plain text in code.
    // Example (Base64 32+ bytes): "p5z...==" (generate securely)
    private static final String SECRET_BASE64 = "REPLACE_WITH_BASE64_SECRET_FROM_ENV";
    private static final long EXPIRY_MILLIS = 15 * 60 * 1000;

    private final SecretKey key;

    public JwtUtil() {
        byte[] keyBytes;

        // If you still want to keep your plain text secret for local testing:
        // keyBytes = "ATLANTA-BANKING-SECRET-KEY-184010012026".getBytes(StandardCharsets.UTF_8);

        // ✅ Better: Base64 decode a properly generated secret
        keyBytes = Decoders.BASE64.decode(SECRET_BASE64);

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails user) {
        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRY_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)        // ✅ use same key used for signing
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails user) {
        try {
            String username = extractUsername(token);
            return username.equals(user.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException covers: expired, malformed, unsupported, signature invalid, etc.
            return false;
        }
    }
}

