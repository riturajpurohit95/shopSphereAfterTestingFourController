package com.ShopSphere.shop_sphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.CartItem;
import com.ShopSphere.shop_sphere.repository.CartItemDao;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	private final CartItemDao cartItemDao;
	
	public CartItemServiceImpl(CartItemDao cartItemDao) {
		this.cartItemDao = cartItemDao;
	}

	@Override
	public CartItem addItem(CartItem cartItem) {
		return cartItemDao.addItem(cartItem);
	}

	@Override
	public Optional<CartItem> findItemByCartId(int cartId) {
		return cartItemDao.findByCartId(cartId);
	}

	@Override
	public List<CartItem> getItemsByCartId(int cartId) {
		return cartItemDao.findAllByCartId(cartId);
	}

	@Override
	public CartItem updateItemQuantity(int cartItemId, int quantity) {
		int rows = cartItemDao.updateItemQuantity(cartItemId, quantity);
		if(rows<=0) {
			throw new ResourceNotFoundException("CatItem update failed or item not found with id: "+cartItemId);
		}
		return cartItemDao.findByCartId(cartItemId)
										.orElseThrow(() -> new ResourceNotFoundException("CartItem not found"))
			;
	}

	@Override
	public void deleteItem(int cartItemId) {
		int rows = cartItemDao.deleteItem(cartItemId);
		if (rows <= 0) {
			throw new ResourceNotFoundException("CartItem not found with id: "+cartItemId);
		}
	}

	@Override
	public void deleteItemByProductId(int cartId, int productId) {
		int rows = cartItemDao.deleteItemByProductId(cartId, productId);
		if(rows<=0) {
			throw new ResourceNotFoundException("No cart item found for productId: "+productId);
		}
	}

	@Override
	public double calculateTotalAmount(int cartId) {
		return cartItemDao.calculateTotalAmount(cartId);
	}

	@Override
	public boolean existsInCart(int cartId, int productId) {
		return cartItemDao.existsInCart(cartId, productId);
	}
	
	

}
