package com.ShopSphere.shop_sphere.security;
 
import com.ShopSphere.shop_sphere.exception.UnauthorizedException;
 
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
 
import org.springframework.stereotype.Component;
 
import java.io.IOException;
 
@Component
public class JwtAuthenticationFilter implements Filter {
 
    private final JwtTokenUtil jwtTokenUtil;
 
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
 
        HttpServletRequest request = (HttpServletRequest) req;
 
        String path = request.getRequestURI();
 
        // allow login/signup without token
        if (path.contains("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }
 
        String token = request.getHeader("Authorization");
 
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid token");
        }
 
        token = token.substring(7);
 
        Claims claims = jwtTokenUtil.validate(token);
 
        request.setAttribute("userId", claims.get("userId"));
        request.setAttribute("role", claims.get("role"));
 
        chain.doFilter(req, res);
    }
}
 
