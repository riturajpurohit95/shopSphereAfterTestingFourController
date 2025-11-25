
package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.ShopSphere.shop_sphere.dto.WishlistItemDto;
import com.ShopSphere.shop_sphere.model.WishlistItem;
import com.ShopSphere.shop_sphere.service.WishlistItemService;

@RestController
@RequestMapping("/api/wishlist-items")
public class WishlistItemController {

    private final WishlistItemService wishlistItemService;

    public WishlistItemController(WishlistItemService wishlistItemService) {
        this.wishlistItemService = wishlistItemService;
    }

    private WishlistItem dtoToEntity(WishlistItemDto dto) {
        WishlistItem wi = new WishlistItem();
        wi.setWishlistItemsId(dto.getWishlistItemsId());
        wi.setWishlistId(dto.getWishlistId());
        wi.setProductId(dto.getProductId());
        return wi;
    }

    private WishlistItemDto entityToDto(WishlistItem wi) {
        WishlistItemDto dto = new WishlistItemDto();
        dto.setWishlistItemsId(wi.getWishlistItemsId());
        dto.setWishlistId(wi.getWishlistId());
        dto.setProductId(wi.getProductId());
        return dto;
    }

    @PostMapping
    public int addItem(@RequestBody WishlistItemDto dto) {
        WishlistItem wi = dtoToEntity(dto);
        return wishlistItemService.addItemToWishlist(wi);
    }

    @GetMapping("/wishlist/{wishlistId}")
    public List<WishlistItemDto> getItemsByWishlist(@PathVariable int wishlistId) {
        return wishlistItemService.getItemsByWishlistId(wishlistId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{wishlistItemId}")
    public String deleteItem(@PathVariable int wishlistItemId) {
        wishlistItemService.deleteItem(wishlistItemId);
        return "Wishlist item deleted successfully";
    }
}
