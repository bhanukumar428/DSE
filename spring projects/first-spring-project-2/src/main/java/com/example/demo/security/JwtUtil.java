package com.example.demo.security;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private final static String SECRET_KEY = "bhanu428"; // Replace with a strong secret

    // Generate JWT Token
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token invalid or expired
        }
    }

    // Extract Email from JWT
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}