package com.ShopSphere.shop_sphere.security;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.exception.BadRequestException;
import com.ShopSphere.shop_sphere.model.User;
import com.ShopSphere.shop_sphere.repository.UserDao;

@Service
public class AuthService {

    private final UserDao userDao;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserDao userDao, JwtTokenUtil jwtTokenUtil) {
        this.userDao = userDao;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String login(String email, String password) {

        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new BadRequestException("Invalid email");
        }

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid password");
        }

        return jwtTokenUtil.generateToken(
                user.getUserId(),
                user.getRole(),
                user.getEmail()
        );
    }
}
