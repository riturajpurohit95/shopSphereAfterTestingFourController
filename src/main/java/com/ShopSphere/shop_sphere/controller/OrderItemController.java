package com.ShopSphere.shop_sphere.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.ShopSphere.shop_sphere.dto.OrderItemDto;
import com.ShopSphere.shop_sphere.model.OrderItem;
import com.ShopSphere.shop_sphere.service.OrderItemsService;

import jakarta.validation.Valid;

@RestController // marking this class as a REST controller, automatically convert responses into JSON format
@RequestMapping("/api/orders") // Sets a base URL for all endpoints in this class
public class OrderItemController {
	
     
	public OrderItemController(OrderItemsService orderItemsService) {
		super();
		this.orderItemsService = orderItemsService;
	}



	private final OrderItemsService orderItemsService;
	
	
	private OrderItem dtoToEntity(OrderItemDto dto) {
		OrderItem item = new OrderItem();
		item.setOrderItemsId(dto.getOrderItemsid());
		item.setOrderId(dto.getOrderId());
		item.setProductId(dto.getProductId());
		item.setProductName(dto.getProductName());
		item.setQuantity(dto.getQuantity());
		item.setUnitPrice(dto.getUnitPrice());
		item.setTotalItemPrice(dto.getTotalItemPrice());
	    
	    
		return item;
	}
	
	// EntitytoDto
	private OrderItemDto entityToDto(OrderItem item) {
		OrderItemDto dto = new OrderItemDto();
	
		dto.setOrderItemsid(item.getOrderItemsId());
		dto.setOrderId(item.getOrderId());
		dto.setProductId(item.getProductId());
		dto.setProductName(item.getProductName());
		dto.setQuantity(item.getQuantity());
		dto.setUnitPrice(item.getUnitPrice());
		dto.setTotalItemPrice(item.getTotalItemPrice());
		return dto;
	}
		

	@PostMapping
	public ResponseEntity<OrderItemDto> createOrderItem(@Valid @RequestBody OrderItemDto dto){
		
		
		OrderItem toCreate = dtoToEntity(dto);
		OrderItem created = orderItemsService.createOrderItem(toCreate);
		return ResponseEntity.created(URI.create("/api/orderItem/" + created.getOrderItemsId()))
				             .body(entityToDto(created));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable("id") int orderItemsId) { 
		
	    OrderItem item = orderItemsService.getOrderItemById(orderItemsId);
		return ResponseEntity.ok(entityToDto(item));		
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable int orderId) { 
		
	    List<OrderItem> items = orderItemsService.getOrderItemsByOrderId(orderId);
	    List<OrderItemDto> dtoList = items.stream().map(this::entityToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);		
	}
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<OrderItemDto>> getOrderItemsByProductId(@PathVariable int productId) { 
		
	    List<OrderItem> items = orderItemsService.getOrderItemsByProductId(productId);
	    List<OrderItemDto> dtoList = items.stream().map(this::entityToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);		
	}
	// Put Request
		@PutMapping("/{id}/quantity")
		public ResponseEntity<OrderItemDto> updateItemQuantity(@PathVariable("id") int orderItemsId, @RequestParam("quantity") int quantity) {		
		
			OrderItem updated = orderItemsService.updateItemQuantity(orderItemsId, quantity);
	        return ResponseEntity.ok(entityToDto(updated));		
		}
	

	
		
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> deleteOrderItem(@PathVariable("id") int orderItemsId) {
			orderItemsService.deleteOrderItem(orderItemsId);
			return ResponseEntity.noContent().build();
		}
		
	
	
	
}
