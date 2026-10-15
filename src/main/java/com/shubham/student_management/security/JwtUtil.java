package com.shubham.student_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    // Values come from application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // Create a signed token for this username
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)                                  // who the token is for
                .issuedAt(now)                                      // when it was made
                .expiration(new Date(now.getTime() + expirationMs)) // when it dies
                .signWith(key)                                      // stamp with our secret
                .compact();
    }

    // Read the username back out of a token (also verifies the signature)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Valid = signature OK + not expired + belongs to this user
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return tokenUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            // bad signature, malformed token, expired, etc.
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Parsing throws an exception if the signature is wrong or the token is expired
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}