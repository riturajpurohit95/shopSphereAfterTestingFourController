package com.ShopSphere.shop_sphere.dto;

import jakarta.validation.constraints.NotNull;

public class CartItemDto {
	
	public CartItemDto() {
		super();
	}
	public CartItemDto(Integer cartItemId, @NotNull(message = "Cart ID is required") Integer cartId,
			@NotNull(message = "Product ID is required") Integer productId,
			@NotNull(message = "Quantity is required") Integer quantity) {
		super();
		this.cartItemId = cartItemId;
		this.cartId = cartId;
		this.productId = productId;
		this.quantity = quantity;
	}
	
	private Integer cartItemId;
	@NotNull(message = "Cart ID is required")
	private Integer cartId;
	@NotNull(message = "Product ID is required")
	private Integer productId;
	@NotNull(message = "Quantity is required")
	private Integer quantity;
	private String productName;
	private Double productPrice;
	private Double totalItemPrice;
	
	public Integer getCartItemId() {
		return cartItemId;
	}
	public Integer getCartId() {
		return cartId;
	}
	public Integer getProductId() {
		return productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public String getProductName() {
		return productName;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public Double getTotalItemPrice() {
		return totalItemPrice;
	}
	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}
	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public void setTotalItemPrice(Double totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}
	
	

}

