package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.WishlistDto;
import com.ShopSphere.shop_sphere.exception.BadRequestException;
import com.ShopSphere.shop_sphere.exception.DuplicateResourceException;
import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Wishlist;
import com.ShopSphere.shop_sphere.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    private WishlistDto entityToDto(Wishlist w) {
        WishlistDto dto = new WishlistDto();
        dto.setWishlistId(w.getWishlistId());
        dto.setUserId(w.getUserId());
        return dto;
    }

    // ---------------- CREATE ----------------
    @PostMapping("/{userId}")
    public ResponseEntity<?> createWishlist(@PathVariable int userId) {
        try {
            Wishlist w = wishlistService.createWishlist(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityToDto(w));
        } catch (DuplicateResourceException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // ---------------- GET BY USER ----------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getWishlistByUserId(@PathVariable int userId) {
        try {
            Wishlist w = wishlistService.getWishlistByUserId(userId);
            return ResponseEntity.ok(entityToDto(w));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{wishlistId}")
    public ResponseEntity<?> getWishlistById(@PathVariable int wishlistId) {
        try {
            Wishlist w = wishlistService.getWishlistById(wishlistId);
            return ResponseEntity.ok(entityToDto(w));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ---------------- FETCH ALL ----------------
    @GetMapping
    public List<WishlistDto> getAllWishlists() {
        return wishlistService.getAllWishlists()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<?> deleteWishlist(@PathVariable int wishlistId) {
        try {
            wishlistService.deleteWishlist(wishlistId);
            return ResponseEntity.ok("Wishlist deleted successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ---------------- EXISTS ----------------
    @GetMapping("/exists/{userId}")
    public ResponseEntity<?> wishlistExistsForUser(@PathVariable int userId) {
        boolean exists = wishlistService.wishlistExistsForUser(userId);
        return ResponseEntity.ok(exists);
    }

    // ---------------- EMPTY CHECK ----------------
    @GetMapping("/empty/{wishlistId}")
    public ResponseEntity<?> isWishlistEmpty(@PathVariable int wishlistId) {
        try {
            boolean empty = wishlistService.isWishlistEmpty(wishlistId);
            return ResponseEntity.ok(empty);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ---------------- GET ITEMS ----------------
    @GetMapping("/items/{userId}")
    public ResponseEntity<?> getWishlistItems(@PathVariable int userId) {
        try {
            List<Map<String, Object>> items = wishlistService.getWishlistItems(userId);

            if (items.isEmpty()) {
                throw new ResourceNotFoundException("No wishlist items found for userId: " + userId);
            }

            return ResponseEntity.ok(items);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
