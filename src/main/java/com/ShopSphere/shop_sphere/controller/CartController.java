package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ShopSphere.shop_sphere.dto.CartDto;
import com.ShopSphere.shop_sphere.model.Cart;
import com.ShopSphere.shop_sphere.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	public Cart dtoToEntity(CartDto cartDto) {
		Cart cart = new Cart();
		cart.setCartId(cartDto.getCartId());
		cart.setUserId(cartDto.getUserId());
		return cart;
	}
	
	private CartDto entityToDto(Cart cart) {
		CartDto cartDto = new CartDto();
		cartDto.setCartId(cart.getCartId());
		cartDto.setUserId(cart.getUserId());
		return cartDto;
	}
	
	@PostMapping("/{userId}")
	public CartDto createCart(@PathVariable int userId) {
		Cart cart = cartService.createCart(userId);
		return entityToDto(cart);
	}
	
	@GetMapping("/user/{userId}")
	public CartDto getCartByUserId(@PathVariable int userId) {
		Cart c = cartService.getCartByUserId(userId);
		return entityToDto(c);
	}
	
	@GetMapping("/{cartId}")
	public CartDto getCartById(@PathVariable int cartId) {
		Cart c = cartService.getCartById(cartId);
		return entityToDto(c);
	}
	
	@GetMapping
	public List<CartDto> getAllCarts(){
		return cartService.getAllCarts()
				.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}
	
	@DeleteMapping("/{cartId}")
	public String deleteCart(@PathVariable int cartId) {
		cartService.deleteCart(cartId);
		return "Cart deleted successfully";
	}
	
	@GetMapping("/exists/{userId}")
	public boolean cartExistsForUser(@PathVariable int userId) {
		return cartService.cartExistsForUser(userId);
	}
	
	@GetMapping("/empty/{cartId}")
	public boolean isCartEmpty(@PathVariable int cartId) {
		return cartService.isCarEmpty(cartId);
	}
	
}






















