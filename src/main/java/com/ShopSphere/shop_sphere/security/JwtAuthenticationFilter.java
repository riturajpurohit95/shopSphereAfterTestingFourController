package com.ShopSphere.shop_sphere.security;
 
import com.ShopSphere.shop_sphere.exception.UnauthorizedException;
 
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
<<<<<<< HEAD
 
=======
import jakarta.servlet.http.HttpServletResponse;

>>>>>>> 9a4dbe4 ("JWT WORKING")
import org.springframework.stereotype.Component;
 
import java.io.IOException;
 
<<<<<<< HEAD
=======
//@Component
//public class JwtAuthenticationFilter implements Filter {
// 
//    private final JwtTokenUtil jwtTokenUtil;
// 
//    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
// 
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
// 
//        HttpServletRequest request = (HttpServletRequest) req;
// 
//        String path = request.getRequestURI();
// 
//        // allow login/signup without token
//        if (path.contains("/api/auth")) {
//            chain.doFilter(req, res);
//            return;
//        }
// 
//        String token = request.getHeader("Authorization");
// 
//        if (token == null || !token.startsWith("Bearer ")) {
//            throw new UnauthorizedException("Missing or invalid token");
//        }
// 
//        token = token.substring(7);
// 
//        Claims claims = jwtTokenUtil.validate(token);
// 
//        request.setAttribute("userId", claims.get("userId"));
//        request.setAttribute("role", claims.get("role"));
// 
//        chain.doFilter(req, res);
//    }
//}
// 
>>>>>>> 9a4dbe4 ("JWT WORKING")
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
<<<<<<< HEAD
 
        String path = request.getRequestURI();
 
        // allow login/signup without token
=======
        HttpServletResponse response = (HttpServletResponse) res;
 
        String path = request.getRequestURI();
 
        // Allow login/signup endpoints without token
>>>>>>> 9a4dbe4 ("JWT WORKING")
        if (path.contains("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }
 
        String token = request.getHeader("Authorization");
<<<<<<< HEAD
 
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid token");
        }
 
        token = token.substring(7);
 
        Claims claims = jwtTokenUtil.validate(token);
 
        request.setAttribute("userId", claims.get("userId"));
        request.setAttribute("role", claims.get("role"));
=======
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid token");
            return;
        }
 
        token = token.substring(7); // Remove "Bearer " prefix
 
        try {
            Claims claims = jwtTokenUtil.validate(token);
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("role", claims.get("role"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token: " + e.getMessage());
            return;
        }
>>>>>>> 9a4dbe4 ("JWT WORKING")
 
        chain.doFilter(req, res);
    }
}
<<<<<<< HEAD
 
=======
 
>>>>>>> 9a4dbe4 ("JWT WORKING")
