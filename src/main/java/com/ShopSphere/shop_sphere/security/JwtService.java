package com.ShopSphere.shop_sphere.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
 
import java.security.Key;
import java.util.Date;
 
@Service
public class JwtService {
 
    private static final String SECRET_KEY = "THIS_IS_A_VERY_LONG_SECRET_KEY_FOR_JWT_256_ENCRYPTION_1234567890";
 
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
 
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
 
    // -------------------------
    // Generate JWT Token
    // -------------------------
    public String generateToken(int userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    // -------------------------
    // Extract Role
    // -------------------------
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
 
    // -------------------------
    // Extract User ID
    // -------------------------
    public Integer extractUserId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }
 
    // -------------------------
    // Extract Email
    // -------------------------
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }
 
    // -------------------------
    // Validate token
    // -------------------------
    public void validateToken(String token) {
        try {
            extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
 
    // -------------------------
    // Claim extraction helper
    // -------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
 

 
