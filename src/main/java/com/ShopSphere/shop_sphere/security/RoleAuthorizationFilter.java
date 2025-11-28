package com.ShopSphere.shop_sphere.security;

import java.io.IOException;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.method.HandlerMethod;

import com.ShopSphere.shop_sphere.exception.ForbiddenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = req.getRequestURI();

        // Allow public endpoints (login, register)
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(req, res);
            return;
        }

        // Spring sets bestMatchingHandler AFTER handler mapping phase,
        // but to use it correctly, you must run this filter AFTER mapping.
        Object handler = req.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        if (handler instanceof HandlerMethod handlerMethod) {

            Method method = handlerMethod.getMethod();
            Class<?> controllerClass = handlerMethod.getBeanType();

            AllowedRoles allowed = null;

            // 1. First check method-level annotation
            if (method.isAnnotationPresent(AllowedRoles.class)) {
                allowed = method.getAnnotation(AllowedRoles.class);
            }
            // 2. Then check class-level annotation
            else if (controllerClass.isAnnotationPresent(AllowedRoles.class)) {
                allowed = controllerClass.getAnnotation(AllowedRoles.class);
            }

            if (allowed != null) {
                String userRole = (String) req.getAttribute("role");

                boolean permitted = false;
                for (String role : allowed.value()) {
                    if (role.equalsIgnoreCase(userRole)) {
                        permitted = true;
                        break;
                    }
                }

                if (!permitted) {
                    throw new ForbiddenException("Role " + userRole + " not allowed for this endpoint");
                }
            }
        }

        filterChain.doFilter(req, res);
    }
}
