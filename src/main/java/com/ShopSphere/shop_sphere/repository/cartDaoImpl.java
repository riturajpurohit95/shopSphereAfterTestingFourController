package com.ShopSphere.shop_sphere.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ShopSphere.shop_sphere.model.Cart;
import com.ShopSphere.shop_sphere.util.CartRowMapper;

@Repository
public class cartDaoImpl implements CartDao{
	
	private final JdbcTemplate jdbcTemplate;
	
	public cartDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int createCart(int userId) {
		String sql = "INSERT INTO carts (user_id) VALUES (?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
				
		jdbcTemplate.update(connection ->{
			PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, userId);
				return ps;
			}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}

	@Override
	public Cart findByUserId(int userId) {
		String sql = "SELECT cart_id, user_id FROM carts WHERE user_id=?";
		
		return jdbcTemplate.queryForObject(sql, new CartRowMapper(), userId);
	}

	@Override
	public Cart findById(int cartId) {
		String sql = "SELECT cart_id, user_id  FROM carts WHERE cart_id=?";
		
		return jdbcTemplate.queryForObject(sql, new CartRowMapper(), cartId);
	}

	@Override
	public List<Cart> getAllCarts() {
		String sql = "SELECT cart_id, user_id FROM carts";
		return jdbcTemplate.query(sql, new CartRowMapper());
	}

	@Override
	public int deleteCart(int cartId) {
		String sql = "DELETE FROM carts WHERE cart_id=?";
		return jdbcTemplate.update(sql,cartId);
	}

	@Override
	public boolean cartExistsForUser(int userId) {
		String sql = "SELECT COUNT(*) FROM carts WHERE user_id=?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
		return count!=null && count>0;
	}

	@Override
	public boolean isCartEmpty(int cartId) {
		String sql = "SELECT COUNT(*) FROM cart_items WHERE cart_id=?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartId);
		return count==null || count==0; 
	}
	
	@Override
	public List<Map<String, Object>> getCartItems(int userId) {
	    String sql = "SELECT ci.cart_items_id,ci.cart_id,ci.quantity, p.product_id, p.product_name, p.product_price " +
	                 "FROM cart_items ci " +
	                 "INNER JOIN products p ON ci.product_id = p.product_id " +
	                 "INNER JOIN carts c ON ci.cart_id = c.cart_id " +
	                 "WHERE c.user_id = ?";

	    return jdbcTemplate.queryForList(sql, userId);
	}
	

}
