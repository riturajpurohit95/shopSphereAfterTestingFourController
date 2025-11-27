package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ShopSphere.shop_sphere.dto.CategoryDto;
import com.ShopSphere.shop_sphere.model.Category;
import com.ShopSphere.shop_sphere.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;
	
	public CategoryController (CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	private Category dtoToEntity(CategoryDto dto) {
		Category category = new Category();
		
		if(dto.getCategoryId() != null) {
		category.setCategoryId(dto.getCategoryId());
		}
		category.setCategoryName(dto.getCategoryName());
		return category;
	}
	
	private CategoryDto entityToDto(Category category) {
		CategoryDto dto = new CategoryDto();
		dto.setCategoryId(category.getCategoryId());
		dto.setCategoryName(category.getCategoryName());
		return dto;
	}
	
	@PostMapping
	public CategoryDto createCategory(@RequestBody CategoryDto dto) {
		Category saved = categoryService.createCategory(dtoToEntity(dto));
		return entityToDto(saved);
	}
	
	
	@GetMapping("/{categoryId}")
	public CategoryDto getCategoryById(@PathVariable int categoryId) {
		Category category = categoryService.getCategoryById(categoryId);
		return entityToDto(category);
	}
	
	@GetMapping 
	public List<CategoryDto> getAllCategories(){
		return categoryService.getAllCategories()
				.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/name/{name}")
	public List<CategoryDto> getCategoryByName(@PathVariable String name){
		return categoryService.getCategoryByName(name)
				.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}
	
	
	@GetMapping("/search/{keyword}")
	public List<CategoryDto> searchCategoryByKeyword(@PathVariable String keyword){
		return categoryService.searchCategoryByKeyword(keyword)
				.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}
	
	@PutMapping("/{categoryId}")
	public CategoryDto updateCategory(
			@PathVariable int categoryId,
			@RequestBody CategoryDto dto) {
		Category updated = categoryService.updateCategory(categoryId, dtoToEntity(dto));
		return entityToDto(updated);
	}

	@DeleteMapping("/{categoryId}")
	public String delteCategory(@PathVariable int categoryId) {
		categoryService.deleteCategory(categoryId);
		return "Category deleted successfully";
	}
	
	
}







