package com.ShopSphere.shop_sphere.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
	
	public CategoryDto(Integer categoryId, @NotBlank(message = "Category name is required") String name) {
		super();
		this.categoryId = categoryId;
		this.name = name;
	}
	
	
	public CategoryDto() {}
	
	private Integer categoryId;
	@NotBlank(message="Category name is required")
	private String name;
	private String description;
	public Integer getCategoryId() {
		return categoryId;
	}
	public String getName() {
		return name;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public void setName(String name) {
		this.name = name;
	}
}