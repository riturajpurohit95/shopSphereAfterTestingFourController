package com.ShopSphere.shop_sphere.repository;

import java.util.List;
import java.util.Optional;

import com.ShopSphere.shop_sphere.model.CartItem;

public interface CartItemDao {
	
	CartItem addItem(CartItem cartItem);
	Optional<CartItem> findByCartId(int cartId);
	int updateItemQuantity(int cartItemId, int quantity);
	int deleteItem(int cartItemId);
	List<CartItem> findAllByCartId(int cartId);
	boolean existsInCart(int cartId, int productId);
	int deleteItemByProductId(int cartId, int productId);
	double calculateTotalAmount(int cartId);

}
