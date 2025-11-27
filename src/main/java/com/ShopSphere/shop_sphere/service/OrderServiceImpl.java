package com.ShopSphere.shop_sphere.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.dto.PaymentDto;
import com.ShopSphere.shop_sphere.exception.OrderAlreadyProcessedException;
import com.ShopSphere.shop_sphere.exception.PaymentAlreadyCompletedException;
import com.ShopSphere.shop_sphere.exception.PaymentMethodNotSupportedException;
import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.model.OrderItem;
import com.ShopSphere.shop_sphere.repository.OrderDao;
import com.ShopSphere.shop_sphere.repository.OrderItemDao;
import com.ShopSphere.shop_sphere.repository.ProductDao;

@Service
public class OrderServiceImpl implements OrderService {

	

	public OrderServiceImpl(OrderDao orderDao, PaymentService paymentService, DeliveryService deliveryService,
			ProductDao productDao, OrderItemDao orderItemDao) {
		super();
		this.orderDao = orderDao;
		this.paymentService = paymentService;
		this.deliveryService = deliveryService;
		this.productDao = productDao;
		this.orderItemDao = orderItemDao;
	}

	private final OrderDao orderDao;
	private final PaymentService paymentService;
	private final DeliveryService deliveryService;
	private final ProductDao productDao;
	private final OrderItemDao orderItemDao;
	
	
	@Override
	public Order createOrder(Order order) {
		if(order == null) {
			throw new IllegalArgumentException("Order must not be null");
		}
		String method = order.getPaymentMethod();
		if(method==null || !(method.equalsIgnoreCase("COD") || method.equalsIgnoreCase("UPI"))) {
			throw new PaymentMethodNotSupportedException(method);
		}
	
		//when the order is first created, initial life cycle, irrespective of payment status
		if(order.getOrderStatus() == null ||  order.getOrderStatus().isEmpty()){
			order.setOrderStatus("PENDING");
		}
		int rows = orderDao.save(order);
		if(rows <=0) {
			throw new RuntimeException("Create failed for order of userId: "+ order.getUserId());
		}
		
		//String method = order.getPaymentMethod();
		//if(method!= null) {
			if("UPI".equalsIgnoreCase(method)) {
				PaymentDto dto = new PaymentDto();
				dto.setOrderId(order.getOrderId());
				dto.setUserId(order.getUserId());
				dto.setAmount(order.getTotalAmount());
				dto.setCurrency("INR");
				dto.setPaymentMethod("UPI");
				paymentService.createPayment(dto);
			}else if("COD".equalsIgnoreCase(method)) {
				PaymentDto dto= new PaymentDto();
				
				dto.setOrderId(order.getOrderId());
				dto.setUserId(order.getUserId());
				dto.setAmount(order.getTotalAmount());
				dto.setCurrency("INR");
				dto.setPaymentMethod("COD");
				paymentService.createPayment(dto);
			}
		//}
		
		
		return orderDao.findById(order.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found after create: "+ order.getOrderId()));
	}
	
	
	
	@Override
	public Order getOrderById(int orderId) {
		return orderDao.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found with id: "+orderId));
	}
	
	@Override
	public List<Order> getOrdersByUserId(int userId){
		return orderDao.findByUserId(userId);
	}
	

	@Override
	public int updateOrderStatus(int OrderId, String orderStatus) {
		//only allow known lifecycle status
		int rows = orderDao.updateOrderStatus(OrderId, orderStatus);
		if(rows <=0) {
			throw new RuntimeException("Update status failed for orderId: "+ OrderId);
		}
	
		return rows;
	}
	// update payment status + auto sync
	@Override
	public int updatePaymentStatus(int orderId, String status) {
		/*if(!"PAID".equalsIgnoreCase(status) && !"REFUNDED".equalsIgnoreCase(status) && !"FAILED".equalsIgnoreCase(status) || !"PENDING".equalsIgnoreCase(status)) {
			throw new PaymentAlreadyCompletedException("Unsupported payment status: "+ status);
		}*/
		
		//get payment for this order
		PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
		
		if(payment == null) {
			throw new RuntimeException("Payment not found for orderId: "+ orderId);
		}
		
	
		//update payment table
		paymentService.updatePaymentStatus(payment.getPayment_id(), status);
		//decide next order status based on payment status
		
		Order order = getOrderById(orderId);
		String currentOrderStatus = order.getOrderStatus();
		String nextOrderStatus = currentOrderStatus;
		
		if("PAID".equalsIgnoreCase(status)) {
			if("PENDING".equalsIgnoreCase(currentOrderStatus)) {
			nextOrderStatus = "PROCESSING";
			}
		}else if ("REFUNDED".equalsIgnoreCase(status)) {
			nextOrderStatus = "REFUNDED";
		}else if("FAILED".equalsIgnoreCase(status)){
			nextOrderStatus = "PENDING";
		}else if("PENDING".equalsIgnoreCase(status)){
			nextOrderStatus = "PENDING";
		}
		int rows = orderDao.updateOrderStatus(orderId, nextOrderStatus);
		if(rows <=0) {
			throw new RuntimeException("Failed to sync order status for Id: "+ orderId);
		}
	
		return rows;
	}
	
	
	@Override
	public Order cancelOrder(int orderId) {
		
		Order existing = getOrderById(orderId);
		
		//if someone tries to cancel a processed order
		if("SHIPPED".equalsIgnoreCase(existing.getOrderStatus()) || "DELIVERED".equalsIgnoreCase(existing.getOrderStatus())) {
		throw new OrderAlreadyProcessedException(orderId, existing.getOrderStatus());
		}	
		PaymentDto payment = null;
		try {
			payment = paymentService.getPaymentByOrderId(orderId);
		}catch(RuntimeException e) {
			
		}
		String finalOrderStatus;
		if(payment != null && "PAID".equalsIgnoreCase(payment.getStatus())) {
			paymentService.updatePaymentStatus(payment.getPayment_id(), "REFUNDED");
			finalOrderStatus = "REFUNDED";
		}else {
			finalOrderStatus = "CANCELLED";
			if(payment != null && !"REFUNDED".equalsIgnoreCase(payment.getStatus())) {
				paymentService.updatePaymentStatus(payment.getPayment_id(), "FAILED");
			}
		}
		
		
		int rows = orderDao.cancelOrder(orderId);
		if(rows <=0) {
			throw new RuntimeException("Cancel failed for orderId: "+ orderId);
		}
		
		return getOrderById(orderId);
	}
	
	@Override
	public void deleteOrder(int OrderId) {
		getOrderById(OrderId);
		
		
		int rows = orderDao.deleteById(OrderId);
		if(rows <=0) {
			throw new RuntimeException("Delete failed for orderId: "+ OrderId);
		}
		
	}
	@Override
	public int placeOrder(int buyerId, int productId) {
		int deliveryDays = deliveryService.calculateDeliveryDays(buyerId, productId);
		System.out.println("Estimated delivery days: "+deliveryDays);
		return deliveryDays;
	}
	@Override
	public List<Map<String, Object>> getOrdersWithItems(int userId) {
        return orderDao.getOrdersWithItems(userId);
    }
	
	@Override 
	public int expireOldPendingOrders() {
		LocalDateTime cutOff = LocalDateTime.now().minusMinutes(10);
		List<Order>OldPendingOrders = orderDao.findByStatusAndPlacedAtBefore("PENDING", cutOff);
		int expiredCount = 0;
		for (Order order: OldPendingOrders) {
			int orderId = order.getOrderId();
			
		List<OrderItem> items = orderItemDao.findByOrderId(orderId);
		for(OrderItem item : items){
		int productId = item.getProductId();
		int quantity = item.getQuantity();
		productDao.increaseStock(productId, quantity);
		
		}
		orderDao.updateOrderStatus(orderId,"EXPIRED");
		expiredCount++;
		
		}
		return expiredCount;
	}
	
	
	
	

	
}
