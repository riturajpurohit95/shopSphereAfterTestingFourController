package com.ShopSphere.shop_sphere.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ShopSphere.shop_sphere.dto.OrderDto;
import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.service.OrderService;


import jakarta.validation.Valid;

@RestController // marking this class as a REST controller, automatically convert responses into JSON format
@RequestMapping("/api/orders") 
public class OrderController {
	
	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}
     
	private final OrderService orderService;
	
	
	private Order dtoToEntity(OrderDto dto) {
		Order order = new Order();
	    order.setOrderId(dto.getOrder_id());
	    order.setUserId(dto.getOrder_id());
	    order.setTotalAmount(dto.getTotalAmount());
	    order.setShippingAddress(dto.getShippingAddress());
	    order.setOrderStatus(dto.getOrderStatus());
	    order.setPlacedAt(dto.getPlacedAt());
	    order.setPaymentMethod(dto.getPaymentMethod());
	    
	    
		return order;
	}
	
	// EntitytoDto
	private OrderDto entityToDto(Order order) {
		OrderDto dto = new OrderDto();
	   dto.setOrder_id(order.getOrderId());
	   dto.setUserId(order.getOrderId());
	   dto.setTotalAmount(order.getTotalAmount());
	   dto.setShippingAddress(order.getShippingAddress());
	   dto.setOrderStatus(order.getOrderStatus());
	   dto.setPlacedAt(order.getPlacedAt());
	   dto.setPaymentMethod(order.getPaymentMethod());
		return dto;
	}
	
	// Get request
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto dto){
		
		
		Order toCreate = dtoToEntity(dto);
		Order created = orderService.createOrder(toCreate);
		return ResponseEntity.created(URI.create("/api/orders/" + created.getOrderId()))
				             .body(entityToDto(created));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") int orderId) { 
		
	    Order order = orderService.getOrderById(orderId);
		return ResponseEntity.ok(entityToDto(order));		
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderByUserId(@PathVariable int userId) { 
		
	    List<Order> orders = orderService.getOrdersByUserId(userId);
	    List<OrderDto> dtoList = orders.stream().map(this::entityToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);		
	}
	
	// Put Request
		@PutMapping("/{id}/status")
		public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("id") int orderId, @RequestParam("orderStatus") String orderStatus) {	
			int rows = orderService.updateOrderStatus(orderId, orderStatus);
			Order updated = orderService.getOrderById(orderId);
	        return ResponseEntity.ok(entityToDto(updated));		
		}
		@PutMapping("/{id}/payment-status")
		public ResponseEntity<OrderDto> updatePaymentStatus(@PathVariable("id") int orderId, @RequestParam("status") String status) {	
		
			orderService.updatePaymentStatus(orderId, status);
			Order updated = orderService.getOrderById(orderId);
	        return ResponseEntity.ok(entityToDto(updated));		
		}
	
		@PostMapping("/{id}/cancel")
		public ResponseEntity<OrderDto> cancelOrder(@PathVariable("id") int orderId){
			Order cancelled = orderService.cancelOrder(orderId);
			return ResponseEntity.ok(entityToDto(cancelled));
		}
	
	
		
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> deleteOrder(@PathVariable("id") int orderId) {
			orderService.deleteOrder(orderId);
			return ResponseEntity.noContent().build();
		}
		
	
		@GetMapping("/estimate")
		public int getEstimatedDelivery(@RequestParam int buyerId, @RequestParam int productId) {
			return orderService.placeOrder(buyerId, productId);
		}
	
		
		@GetMapping("/userOrder/{userId}")
	    public ResponseEntity<?> getOrdersWithItems(@PathVariable int userId) {
	        List<Map<String, Object>> result = orderService.getOrdersWithItems(userId);

	        if (result.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No orders found for userId: " + userId);
	        }

	        return ResponseEntity.ok(result);
		}
}
	
	
	
	
	
		
		
		
	