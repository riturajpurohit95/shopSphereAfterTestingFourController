package com.ShopSphere.shop_sphere.security;
 
import com.ShopSphere.shop_sphere.exception.ForbiddenException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.reflect.Method;
 
@Component
public class RoleAuthorizationFilter implements Filter {
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
 
        HttpServletRequest request = (HttpServletRequest) req;
 
        String role = (String) request.getAttribute("role");
 
        // No JWT? Skip
        if (role == null) {
            chain.doFilter(req, res);
            return;
        }
 
        // Check @AllowedRoles annotation on controller method
        HandlerMethod method = (HandlerMethod) request.getAttribute("handlerMethod");
 
        if (method != null) {
            AllowedRoles annotation = method.getMethod().getAnnotation(AllowedRoles.class);
            if (annotation != null) {
                boolean allowed = false;
                for (String r : annotation.value()) {
                    if (r.equalsIgnoreCase(role)) {
                        allowed = true;
                        break;
                    }
                }
                if (!allowed) {
                    throw new ForbiddenException("Access denied: Role not allowed");
                }
            }
        }
 
        chain.doFilter(req, res);
    }
}
 
