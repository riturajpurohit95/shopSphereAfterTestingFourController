
package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.WishlistDto;
import com.ShopSphere.shop_sphere.model.Wishlist;
import com.ShopSphere.shop_sphere.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    private Wishlist dtoToEntity(WishlistDto dto) {
        Wishlist w = new Wishlist();
        w.setWishlistId(dto.getWishlistId());
        w.setUserId(dto.getUserId());
        return w;
    }

    private WishlistDto entityToDto(Wishlist w) {
        WishlistDto dto = new WishlistDto();
        dto.setWishlistId(w.getWishlistId());
        dto.setUserId(w.getUserId());
        return dto;
    }

    @PostMapping("/{userId}")
    public WishlistDto createWishlist(@PathVariable int userId) {
        Wishlist w = wishlistService.createWishlist(userId);
        return entityToDto(w);
    }

    @GetMapping("/user/{userId}")
    public WishlistDto getWishlistByUserId(@PathVariable int userId) {
        Wishlist w = wishlistService.getWishlistByUserId(userId);
        return entityToDto(w);
    }

    @GetMapping("/{wishlistId}")
    public WishlistDto getWishlistById(@PathVariable int wishlistId) {
        Wishlist w = wishlistService.getWishlistById(wishlistId);
        return entityToDto(w);
    }

    @GetMapping
    public List<WishlistDto> getAllWishlists() {
        return wishlistService.getAllWishlists()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{wishlistId}")
    public String deleteWishlist(@PathVariable int wishlistId) {
        wishlistService.deleteWishlist(wishlistId);
        return "Wishlist deleted successfully";
    }

    @GetMapping("/exists/{userId}")
    public boolean wishlistExistsForUser(@PathVariable int userId) {
        return wishlistService.wishlistExistsForUser(userId);
    }

    @GetMapping("/empty/{wishlistId}")
    public boolean isWishlistEmpty(@PathVariable int wishlistId) {
        return wishlistService.isWishlistEmpty(wishlistId);
    }
    
    @GetMapping("/items/{userId}")
    public ResponseEntity<?> getWishlistItems(@PathVariable int userId) {
        List<Map<String, Object>> items = wishlistService.getWishlistItems(userId);

        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("No wishlist items found for userId: " + userId);
        }

        return ResponseEntity.ok(items);
    }
    
}
