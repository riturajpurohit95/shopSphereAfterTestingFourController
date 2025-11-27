package com.ShopSphere.shop_sphere.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ShopSphere.shop_sphere.model.CartItem;
import com.ShopSphere.shop_sphere.util.CartItemRowMapper;

@Repository
public class CartItemDaoImpl implements CartItemDao{
	
	private final JdbcTemplate jdbcTemplate;
	
	public CartItemDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public CartItem addItem(CartItem cartItem) {
		
		//prevent duplicate entry of product and will update the quantity of already existing product
		//prevent adding same product twice in the same cart
		//trg_cart_item_unique  -- trigger will also help
		Optional<CartItem> existingItemOpt = findAllByCartId(cartItem.getCartId()).stream()
					.filter(ci -> 
							ci.getProductId() == cartItem.getProductId())
																.findFirst();
		if (existingItemOpt.isPresent()) {
			CartItem existingItem = existingItemOpt.get();
			int newQuantity = existingItem.getQuantity() + cartItem.getQuantity();
			
			updateItemQuantity(existingItem.getCartItemsId(),newQuantity);
			
			existingItem.setQuantity(newQuantity);
			return existingItem;
		}
		else {
		
			String sql = "INSERT INTO cart_items(cart_id, product_id, quantity)"
					+ "VALUES(?, ?, ?)";
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
					
			jdbcTemplate.update(connection ->{
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, cartItem.getCartId());
					ps.setInt(2, cartItem.getProductId());
					ps.setInt(3, cartItem.getQuantity());
					
					return ps;
				}, keyHolder);
			
			return cartItem;
		}
	}

	
	@Override
		public Optional<CartItem> findByCartId(int cartId) {
			String sql = "SELECT * from cart_items WHERE  cart_items_id=?";
			List<CartItem> result = jdbcTemplate.query(sql, new CartItemRowMapper(), cartId);
			if(result.isEmpty()) return Optional.empty();
			return Optional.of(result.get(0));
		}

	@Override
	public int updateItemQuantity(int cartItemId, int quantity) {
		String sql = "UPDATE cart_items SET quantity=? WHERE cart_items_id=?";
		
		return jdbcTemplate.update(sql,
				quantity,
				cartItemId
				
				);
	}

	@Override
	public int deleteItem(int cartItemId) {
		String sql = "DELETE FROM cart_items WHERE cart_items_id = ?";
		return jdbcTemplate.update(sql,cartItemId);
	}
	

	@Override
	public List<CartItem> findAllByCartId(int cartId) {
		String sql = "SELECT * from cart_items WHERE  cart_id=?";
		return jdbcTemplate.query(sql, new CartItemRowMapper(), cartId);
	}

	@Override
	public boolean existsInCart(int cartId, int productId) {
		String sql = "SELECT COUNT(*) FROM cart_items WHERE cart_id=? AND product_id=?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartId, productId);
		return count!=null && count>0;
	}

	@Override
	public int deleteItemByProductId(int cartId, int productId) {
		String sql = "DELETE FROM cart_items WHERE cart_id =? AND product_id";
		return jdbcTemplate.update(sql, cartId, productId);
	}

	@Override
	public double calculateTotalAmount(int cartId) {
		String sql = "SELECT SUM(p.product_price * ci.quantity)"+" FROM cart_items ci"
					+" JOIN products p ON ci.product_id = p.product_id "+"WHERE ci.cart_id=?";
		Double total = jdbcTemplate.queryForObject(sql,	Double.class, cartId);
		return total!=null ? total:0.0;
	}
	
	

}
