package com.ShopSphere.shop_sphere.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil {

    private final String SECRET = "MySecretKeyForJwtTokenGenerationShopSphere12345"; // >= 32 chars
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 hours

    // Generate JWT token
    public String generateToken(int userId, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes()) // older version
                .compact();
    }

    // Validate token
    public boolean isValid(String token) {
        try {
            Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    // Get claims
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public int getUserId(String token) {
        return Integer.parseInt(getClaims(token).getSubject());
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }
}
