
package com.ShopSphere.shop_sphere.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Wishlist;
import com.ShopSphere.shop_sphere.repository.WishlistDao;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistDao wishlistDao;

    public WishlistServiceImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    @Override
    public Wishlist createWishlist(int userId) {
        if (wishlistDao.wishlistExistsForUser(userId)) {
            throw new RuntimeException("Wishlist already exists for userId: " + userId);
        }
        int wishlistId = wishlistDao.createWishlist(userId);
        return wishlistDao.findById(wishlistId);
    }

    @Override
    public Wishlist getWishlistByUserId(int userId) {
        Wishlist wishlist = wishlistDao.findByUserId(userId);
        if (wishlist == null) {
            throw new ResourceNotFoundException("No wishlist found for userId: " + userId);
        }
        return wishlist;
    }

    @Override
    public Wishlist getWishlistById(int wishlistId) {
        Wishlist wishlist = wishlistDao.findById(wishlistId);
        if (wishlist == null) {
            throw new ResourceNotFoundException("No wishlist found for wishlistId: " + wishlistId);
        }
        return wishlist;
    }

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistDao.getAllWishlists();
    }

    @Override
    public void deleteWishlist(int wishlistId) {
        // ensure it exists
        Wishlist existing = getWishlistById(wishlistId);
        int rows = wishlistDao.deleteWishlist(existing.getWishlistId());
        if (rows <= 0) {
            throw new RuntimeException("Delete failed for wishlistId: " + wishlistId);
        }
    }

    @Override
    public boolean wishlistExistsForUser(int userId) {
        return wishlistDao.wishlistExistsForUser(userId);
    }

    @Override
    public boolean isWishlistEmpty(int wishlistId) {
        Wishlist existing = getWishlistById(wishlistId);
        return wishlistDao.isWishlistEmpty(existing.getWishlistId());
    }
    
    @Override
    public List<Map<String, Object>> getWishlistItems(int userId) {
        return wishlistDao.getWishlistItems(userId);
    }
    
}
