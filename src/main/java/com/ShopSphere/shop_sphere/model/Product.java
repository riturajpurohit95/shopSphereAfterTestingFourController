package com.ShopSphere.shop_sphere.model;

import java.math.BigDecimal;

public class Product {
	
	

	public Product(String brand, String description) {
		super();
		Brand = brand;
		this.description = description;
	}

	public Product(int userId, Integer categoryId, String productName, BigDecimal productPrice, BigDecimal productMrp,
			Integer productQuantity, BigDecimal productAvgRating, Integer productReviewsCount) {
		super();
		this.userId = userId;
		this.categoryId = categoryId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productMrp = productMrp;
		this.productQuantity = productQuantity;
		this.productAvgRating = productAvgRating;
		this.productReviewsCount = productReviewsCount;
	}

	private int productId;
	private int userId;
	private Integer categoryId;
	private String productName;
	private BigDecimal productPrice;
	private BigDecimal productMrp;
	private Integer productQuantity;
	private BigDecimal productAvgRating;
	private Integer productReviewsCount;
	private String Brand;
	private String description;
	
	public Product() {}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getProductMrp() {
		return productMrp;
	}

	public void setProductMrp(BigDecimal productMrp) {
		this.productMrp = productMrp;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public BigDecimal getProductAvgRating() {
		return productAvgRating;
	}

	public void setProductAvgRating(BigDecimal productAvgRating) {
		this.productAvgRating = productAvgRating;
	}

	public Integer getProductReviewsCount() {
		return productReviewsCount;
	}

	public void setProductReviewsCount(Integer productReviewsCount) {
		this.productReviewsCount = productReviewsCount;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", userId=" + userId + ", categoryId=" + categoryId
				+ ", productName=" + productName + ", productPrice=" + productPrice + ", productMrp=" + productMrp
				+ ", productQuantity=" + productQuantity + ", productAvgRating=" + productAvgRating
				+ ", productReviewsCount=" + productReviewsCount + "]";
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

}
