package com.ShopSphere.shop_sphere.repository;

import java.util.List;

import com.ShopSphere.shop_sphere.model.Order;
import java.util.Optional;

public interface OrderDao {
	
	int save(Order order);
	Optional<Order> findById(int orderId);
	List<Order> findByUserId(int userId);
	
	int updateOrderStatus(int orderId, String orderStatus);
	int updatePaymentStatus(int orderId, String status);
	int cancelOrder(int orderId);
	int deleteById(int orderId);
	
	
	
	
    
}
