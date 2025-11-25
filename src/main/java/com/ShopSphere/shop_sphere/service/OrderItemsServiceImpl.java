package com.ShopSphere.shop_sphere.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;

import com.ShopSphere.shop_sphere.model.OrderItem;
import com.ShopSphere.shop_sphere.repository.OrderItemDao;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {
	
	public OrderItemsServiceImpl(OrderItemDao orderItemDao) {
		super();
		this.orderItemDao = orderItemDao;
	}

	private final OrderItemDao orderItemDao ;
	
	
	@Override
	public OrderItem createOrderItem(OrderItem item) {
		if(item== null) {
			throw new IllegalArgumentException("Order item must not be null");
		}
		int rows = orderItemDao.save(item);
		if(rows <=0) {
			throw new RuntimeException("Create failed for order item ");
		}
		return item;
	}
	
	
	
	@Override
	public OrderItem getOrderItemById(int orderItemsId) {
		return orderItemDao.findById(orderItemsId).orElseThrow(()-> new ResourceNotFoundException("Order item not found with id: "+orderItemsId));
	}
	
	@Override
	public List<OrderItem> getOrderItemsByOrderId(int orderId){
		return orderItemDao.findByOrderId(orderId);
	}
	@Override
	public List<OrderItem> getOrderItemsByProductId(int productId){
		return orderItemDao.findByProductId(productId);
	}
	
	

	@Override
	public OrderItem updateItemQuantity(int OrderItemsId, int quantity) {
		
		
		if(quantity <=0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
			
		}
		OrderItem existing = getOrderItemById(OrderItemsId);
		BigDecimal unitPrice = existing.getUnitPrice();
		BigDecimal totalPrice =  unitPrice.multiply(BigDecimal.valueOf(quantity));
		
		int rows = orderItemDao.updateQuantityANDTotalPrice(OrderItemsId, quantity, totalPrice );
		if(rows <=0) {
			throw new RuntimeException("Update failed for order item Id: "+ OrderItemsId);
		}
		existing.setQuantity(quantity);
		existing.setTotalItemPrice(totalPrice);
		return existing;
	}
	

	
	@Override
	public void deleteOrderItem(int OrderItemsId) {
		getOrderItemById(OrderItemsId);
		
		
		int rows = orderItemDao.deleteById(OrderItemsId);
		if(rows <=0) {
			throw new RuntimeException("Delete failed for order Item Id: "+ OrderItemsId);
		}
		
	}
	
	

}
