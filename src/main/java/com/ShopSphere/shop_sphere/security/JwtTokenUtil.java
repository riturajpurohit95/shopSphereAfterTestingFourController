package com.ShopSphere.shop_sphere.security;
 
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
 
import java.util.Date;
 
@Component
public class JwtTokenUtil {
 
    private final String SECRET = "MY_SECRET_KEY_123";
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours
 
    public String generateToken(int userId, String role, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
 
    public Claims validate(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
}
 
