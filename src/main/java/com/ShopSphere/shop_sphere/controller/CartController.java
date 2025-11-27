package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.CartDto;
import com.ShopSphere.shop_sphere.dto.CartItemDto;
import com.ShopSphere.shop_sphere.model.Cart;
import com.ShopSphere.shop_sphere.response.ApiResponse;
import com.ShopSphere.shop_sphere.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private CartDto entityToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setUserId(cart.getUserId());
        return cartDto;
    }


    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createCart(@PathVariable int userId) {

        Cart cart = cartService.createCart(userId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart created successfully", entityToDto(cart))
        );
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable int userId) {

        Cart cart = cartService.getCartByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart fetched successfully", entityToDto(cart))
        );
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable int cartId) {

        Cart cart = cartService.getCartById(cartId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart fetched successfully", entityToDto(cart))
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getAllCarts() {

        List<CartDto> carts = cartService.getAllCarts()
                                         .stream()
                                         .map(this::entityToDto)
                                         .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Carts fetched successfully", carts)
        );
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable int cartId) {

        cartService.deleteCart(cartId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart deleted successfully")
        );
    }


    @GetMapping("/exists/{userId}")
    public ResponseEntity<ApiResponse> cartExistsForUser(@PathVariable int userId) {

        boolean exists = cartService.cartExistsForUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart existence checked", exists)
        );
    }



    @GetMapping("/userCart/{userId}")
    public ResponseEntity<ApiResponse> getCartItems(@PathVariable int userId) {

        List<CartItemDto> items = cartService.getCartItemsByUserId(userId);

        return ResponseEntity.ok(
                ApiResponse.success("Cart items fetched successfully", items)
        );
    }

}























