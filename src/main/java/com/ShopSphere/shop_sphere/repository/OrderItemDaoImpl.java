package com.ShopSphere.shop_sphere.repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.model.OrderItem;
import com.ShopSphere.shop_sphere.util.OrderItemRowMapper;
import com.ShopSphere.shop_sphere.util.OrderRowMapper;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
	
	public OrderItemDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	    public int save(OrderItem orderItem) {    	
	    	
	    	 String sql = "INSERT INTO order_items (order_id,product_id,quantity,unit_price,total_item_price) " +
	                "VALUES (?, ?, ?, ?, ?)";
	         KeyHolder keyHolder = new GeneratedKeyHolder(); 
	         jdbcTemplate.update(connection -> {                   
	            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            ps.setInt(1, orderItem.getOrderId());
	            ps.setInt(2, orderItem.getProductId());
	            ps.setInt(3, orderItem.getQuantity());
	            ps.setBigDecimal(4, orderItem.getUnitPrice());
	            ps.setBigDecimal(5, orderItem.getTotalItemPrice());
	         
	            return ps;
	            }, keyHolder);   
	            Number key = keyHolder.getKey();
	            if (key != null) {
	            	orderItem.setOrderItemsId(key.intValue());
	            }
	            return orderItem.getOrderItemsId(); 		
	    }
		//find by order id
	
		@Override
		public Optional<OrderItem> findById(int orderItemsId) {		
			String sql = "select * from order_items where order_items_id = ?";
			List<OrderItem> list = jdbcTemplate.query(sql, new OrderItemRowMapper(),orderItemsId);
			if (list.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(list.get(0));		
		}
		 //find by order id
		
		@Override
		public List<OrderItem> findByOrderId(int orderId) {		
			String sql = "select * from order_items where order_id=? ";
			return jdbcTemplate.query(sql, new OrderItemRowMapper(), orderId);
			
					
		}
 //find by product id

		@Override
		public List<OrderItem> findByProductId(int productId) {		
			String sql = "select * from order_items where product_id=? ";
			return jdbcTemplate.query(sql, new OrderItemRowMapper(), productId);
			
					
		}
		// update Quantity AND TotalPrice
      @Override
      public int updateQuantityANDTotalPrice(int orderItemsId,int quantity,BigDecimal totalPrice) {
  		String sql = "Update order_items SET quantity=? ,total_item_price=?  where order_items_id=? ";
  		return  jdbcTemplate.update(sql, quantity,totalPrice,orderItemsId);
      }
		
		//delete order item
		@Override
		public int deleteById(int orderItemsId) {
			String sql = "delete from order_items where order_items_id=?";
			return jdbcTemplate.update(sql,orderItemsId);
		}
		
	
}
