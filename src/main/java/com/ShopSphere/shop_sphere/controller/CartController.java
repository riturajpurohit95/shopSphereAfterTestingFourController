package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.CartDto;
import com.ShopSphere.shop_sphere.dto.CartItemDto;
import com.ShopSphere.shop_sphere.model.Cart;
import com.ShopSphere.shop_sphere.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ---------------- Helper Mapping Methods ----------------

//    private Cart dtoToEntity(CartDto cartDto) {
//        Cart cart = new Cart();
//        cart.setCartId(cartDto.getCartId());
//        cart.setUserId(cartDto.getUserId());
//        return cart;
//    }
    
//    Using dtoToEntity() in CartController adds dead code, potential security 
//    issues, and no actual benefit. It’s safer, cleaner, and more maintainable to omit 
//    it entirely in this context.

    private CartDto entityToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setUserId(cart.getUserId());
        return cartDto;
    }

    // ---------------------- API Endpoints ------------------------

    // Create Cart for User
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> createCart(@PathVariable int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        Cart cart = cartService.createCart(userId);
        return ResponseEntity.ok(entityToDto(cart));
    }

    // Get Cart by UserID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(entityToDto(cart));
    }

    // Get Cart by CartID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable int cartId) {
        if (cartId <= 0) {
            throw new IllegalArgumentException("Invalid cart ID");
        }

        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(entityToDto(cart));
    }

    // Get All Carts
    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        List<CartDto> carts = cartService.getAllCarts()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(carts);
    }

    // Delete Cart
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable int cartId) {
        if (cartId <= 0) {
            throw new IllegalArgumentException("Invalid cart ID");
        }

        cartService.deleteCart(cartId);
        return ResponseEntity.ok("Cart deleted successfully");
    }

    // Check if Cart exists for User
    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> cartExistsForUser(@PathVariable int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return ResponseEntity.ok(cartService.cartExistsForUser(userId));
    }

    // Get Cart Items for a User
    @GetMapping("/userCart/{userId}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
    }
}
