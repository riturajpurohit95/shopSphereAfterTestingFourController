package com.ShopSphere.shop_sphere.security;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowedRoles {
    String[] value();
}

