package com.ShopSphere.shop_sphere.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.util.OrderRowMapper;

@Repository
public class OrderDaoImpl implements OrderDao {

	private final JdbcTemplate jdbcTemplate;
	
	public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	    public int save(Order order) {    	
	    	
	    	 String sql = "INSERT INTO orders (user_id, total_amount, shipping_address, status,placed_at, paymentMethod) " +
	                "VALUES (?, ?, ?, ?, ?,?)";
	         KeyHolder keyHolder = new GeneratedKeyHolder(); 
	         jdbcTemplate.update(connection -> {                   
	            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            ps.setInt(1, order.getUserId());
	            ps.setBigDecimal(2, order.getTotalAmount());
	            ps.setString(3, order.getShippingAddress());
	            ps.setString(4, order.getOrderStatus());
	            ps.setTimestamp(5, Timestamp.valueOf(order.getPlacedAt()));
	            ps.setString(6, order.getPaymentMethod());
	            return ps;
	            }, keyHolder);   
	            Number key = keyHolder.getKey();
	            if (key != null) {
	        	   order.setOrderId(key.intValue());
	            }
	            return order.getOrderId(); 		
	    }
		//find by order id
	
		@Override
		public Optional<Order> findById(int orderId) {		
			String sql = "select * from orders where order_id = ?";
			List<Order> list = jdbcTemplate.query(sql, new OrderRowMapper(),orderId);
			if (list.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(list.get(0));		
		}
		 //find by user id
		
		@Override
		public List<Order> findByUserId(int userId) {		
			String sql = "select * from orders where user_id=?  ORDER BY placed_at DESC";
			List<Order> list = jdbcTemplate.query(sql, new OrderRowMapper(), userId);
			return list;
					
		}
		
       //update status using order id
		@Override
		public int updateOrderStatus(int orderId, String orderStatus) {
			String sql="Update orders SET status =? where order_id=?";
			return jdbcTemplate.update(sql, orderStatus, orderId);
		}
		//update payment status using order id
		public int updatePaymentStatus(int orderId, String status) {
			String sql="Update orders SET status =? where order_id=?";
			return jdbcTemplate.update(sql, status, orderId);
		}
		
		//cancel order
		@Override
		public int cancelOrder(int orderId) {
			String sql="Update orders SET status ='CANCELLED' where order_id=?";
			return jdbcTemplate.update(sql,orderId);
		}
		
		//delete order
		@Override
		public int deleteById(int orderId) {
			String sql = "delete from orders where order_id=?";
			return jdbcTemplate.update(sql,orderId);
		}
		
		@Override
		public List<Order> findByStatusAndPlacedAtBefore(String status, LocalDateTime cutOffTime){
			String sql ="Select * from orders where status =? and placed_at < ?";
			return jdbcTemplate.query( sql,  new OrderRowMapper(), status, Timestamp.valueOf(cutOffTime));
		}
		@Override
		public int updateRazorpayOrderId(int orderId, String razorpayOrderId) {
			String sql="Update orders set razorpay_order_id =? where order_id =?";
			return jdbcTemplate.update(sql, razorpayOrderId, orderId);
		}
		
		@Override
		public List<Map<String, Object>> getOrdersWithItems(int userId) {
	        String sql = "SELECT o.order_id, o.total_amount, o.shipping_address, " +
	                     "oi.product_name, oi.quantity, oi.unit_price " +
	                     "FROM orders o " +
	                     "INNER JOIN order_items oi ON o.order_id = oi.order_id " +
	                     "WHERE o.user_id = ?";

	        return jdbcTemplate.queryForList(sql, userId);
		}
		
		
		
}
