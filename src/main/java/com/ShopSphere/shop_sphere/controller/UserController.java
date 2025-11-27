package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.UserDto;
import com.ShopSphere.shop_sphere.model.User;
import com.ShopSphere.shop_sphere.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    // Constructor injection (preferred)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // -------- Mappers (Model <-> DTO) --------

    private User dtoToEntity(UserDto dto) {
        User u = new User();
        u.setUserId(dto.getUserId() != null ? dto.getUserId() : 0);
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        // NOTE: UserDto intentionally does NOT carry password. If you need it for create/update,
        // accept a separate request DTO like UserRegisterRequest and map password there.
        u.setPhone(dto.getPhone());
        u.setRole(dto.getRole());
        u.setLocationId(dto.getLocationId());
        return u;
    }

    private UserDto entityToDto(User u) {
        UserDto dto = new UserDto();
        dto.setUserId(u.getUserId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        // Do NOT expose password in responses
        dto.setPhone(u.getPhone());
        dto.setRole(u.getRole());
        dto.setLocationId(u.getLocationId());
        return dto;
    }

    // -------- Endpoints --------

    @PostMapping
    public int createUser(@Valid @RequestBody UserDto dto) {
        // If your service requires a password, this endpoint should accept a different DTO.
        // Here we create a user with fields present in UserDto only.
        return userService.createUser(dtoToEntity(dto));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        User u = userService.getUserById(userId);
        if (u == null) {
            // You could throw a custom NotFoundException and handle via @ControllerAdvice
            // For now, let it return null or change to 404 handling as needed.
            return null;
        }
        return entityToDto(u);
    }

    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        User u = userService.getUserByEmail(email);
        return u != null ? entityToDto(u) : null;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId, @Valid @RequestBody UserDto dto) {
        User u = dtoToEntity(dto);
        u.setUserId(userId); // trust path param as source of truth
        int rows = userService.updateUser(u);
        return rows > 0 ? "User updated successfully" : "No changes";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "User deleted successfully";
    }

    @GetMapping("/exists/{email}")
    public boolean userExists(@PathVariable String email) {
        return userService.userExistsByEmail(email);
    }
    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable int userId) {
        try {
            Map<String, Object> userProfile = userService.getUserWithLocation(userId);
            return ResponseEntity.ok(userProfile);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found with id: " + userId);
        }
    }
}