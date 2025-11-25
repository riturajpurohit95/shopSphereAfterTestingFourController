package com.ShopSphere.shop_sphere.repository;

import java.util.List;

import com.ShopSphere.shop_sphere.model.Cart;

public interface CartDao {
	
	int createCart(int userId);
	Cart findByUserId(int userId);
	Cart findById(int cartId);
	List<Cart> getAllCarts();
	int deleteCart(int cartId);
	boolean cartExistsForUser(int userId);
	boolean isCartEmpty(int cartId);

}
