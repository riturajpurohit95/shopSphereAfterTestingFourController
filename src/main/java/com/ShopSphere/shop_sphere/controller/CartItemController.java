package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.CartItemDto;
import com.ShopSphere.shop_sphere.model.CartItem;
import com.ShopSphere.shop_sphere.service.CartItemService;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    private CartItem dtoToEntity(CartItemDto dto) {
        CartItem item = new CartItem();
        item.setCartItemsId(dto.getCartItemId());
        item.setCartId(dto.getCartId());
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    private CartItemDto entityToDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setCartItemId(item.getCartItemsId());
        dto.setCartId(item.getCartId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    @PostMapping
    public CartItemDto addItem(@RequestBody CartItemDto dto) {
        CartItem saved = cartItemService.addItem(dtoToEntity(dto));
        return entityToDto(saved);
    }

    @GetMapping("/cart/{cartId}")
    public List<CartItemDto> getItemsByCartId(@PathVariable int cartId) {
        return cartItemService.getItemsByCartId(cartId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/single/{cartId}")
    public CartItemDto findItemByCartId(@PathVariable int cartId) {
        Optional<CartItem> item = cartItemService.findItemByCartId(cartId);
        return item.map(this::entityToDto)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @PutMapping("/{cartItemId}/quantity/{quantity}")
    public CartItemDto updateItemQuantity(@PathVariable int cartItemId,
                                          @PathVariable int quantity) {
        return entityToDto(cartItemService.updateItemQuantity(cartItemId, quantity));
    }

    @DeleteMapping("/{cartItemId}")
    public String deleteItem(@PathVariable int cartItemId) {
        cartItemService.deleteItem(cartItemId);
        return "Cart item deleted successfully";
    }

    @GetMapping("/total/{cartId}")
    public double calculateTotalAmount(@PathVariable int cartId) {
        return cartItemService.calculateTotalAmount(cartId);
    }

    @GetMapping("/exists/{cartId}/{productId}")
    public boolean existsInCart(@PathVariable int cartId, @PathVariable int productId) {
        return cartItemService.existsInCart(cartId, productId);
    }
}
