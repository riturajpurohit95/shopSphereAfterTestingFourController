package com.ShopSphere.shop_sphere.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.dto.CartItemDto;
import com.ShopSphere.shop_sphere.exception.CartNotFoundException;
import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Cart;
import com.ShopSphere.shop_sphere.repository.CartDao;

@Service
public class CartServiceImpl implements CartService{
	
	private final CartDao cartDao;
	
	public CartServiceImpl(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	@Override
	public Cart createCart(int userId) {
		if (cartDao.cartExistsForUser(userId)) {
			throw new RuntimeException("Cart already exists for userId: "+userId);
		}
		int cartId = cartDao.createCart(userId);
		return cartDao.findById(cartId);
	}

	@Override
	public Cart getCartByUserId(int userId) {
		Cart cart = cartDao.findByUserId(userId);
		
		if(cart == null) {
			throw new ResourceNotFoundException("No cart found for userId: "+userId);
		}
		return cart;
	}

	@Override
	public Cart getCartById(int cartId) {
		Cart cart = cartDao.findById(cartId);
		
		if(cart == null) {
			throw new CartNotFoundException("No cart found for userId: "+cartId);
		}
		return cart;
	}

	@Override
	public List<Cart> getAllCarts() {
		return cartDao.getAllCarts();
	}

	@Override
	public void deleteCart(int cartId) {
		Cart existing = getCartById(cartId);
		int rows = cartDao.deleteCart(existing.getCartId());
		
		if (rows<=0) {
			throw new RuntimeException("Delete failed for cartId: "+cartId);
		}
	}

	@Override
	public boolean cartExistsForUser(int userId) {
		return cartDao.cartExistsForUser(userId);
	}

	@Override
	public boolean isCarEmpty(int cartId) {
		Cart existing = getCartById(cartId);
		return cartDao.isCartEmpty(existing.getCartId());
	}
	
	@Override
    public List<CartItemDto> getCartItemsByUserId(int userId) {

        List<Map<String, Object>> rows = cartDao.getCartItems(userId);

        return rows.stream().map(r -> {
            int cartItemsId = ((Number) r.get("cart_items_id")).intValue();
            int cartId =((Number)r.get("cart_id")).intValue();
            int quantity = ((Number)r.get("quantity")).intValue();
            int productId = ((Number) r.get("product_id")).intValue();
            String productName = r.get("product_name").toString();
            double productPrice = ((Number) r.get("product_price")).doubleValue();

            return new CartItemDto(cartItemsId, cartId, productId,quantity, productName, productPrice);

        }).collect(Collectors.toList());
    }
	

}
