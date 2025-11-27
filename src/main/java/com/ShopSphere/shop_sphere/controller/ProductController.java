package com.ShopSphere.shop_sphere.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
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

import com.ShopSphere.shop_sphere.dto.OrderDto;
import com.ShopSphere.shop_sphere.dto.ProductDto;
import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.model.Product;
import com.ShopSphere.shop_sphere.service.OrderService;
import com.ShopSphere.shop_sphere.service.ProductService;

import jakarta.validation.Valid;

@RestController // marking this class as a REST controller, automatically convert responses into JSON format
@RequestMapping("/api/products") // Sets a base URL for all endpoints in this class
public class ProductController {
	
	
     
	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}



	private final ProductService productService;
	
	
	private Product dtoToEntity(ProductDto dto) {
		Product product = new Product();
		//product.setProductId(dto.getProduct_id());
		product.setUserId(dto.getUserId());
		product.setCategoryId(dto.getCategoryId());
		product.setProductName(dto.getProductName());
		product.setProductPrice(dto.getProductPrice());
		product.setProductMrp(dto.getProductMrp());
		product.setProductQuantity(dto.getProductQuantity());
		product.setProductAvgRating(dto.getProductAvgRating());
		product.setProductReviewsCount(dto.getProductReviewsCount());
		product.setBrand(dto.getBrand());
		product.setProductDescription(dto.getProductDescription());
		product.setImageUrl(dto.getImageUrl());
	    
		return product;
	}
	
	// EntitytoDto
	private ProductDto entityToDto(Product product) {
		ProductDto dto = new ProductDto();
	    dto.setProduct_id(product.getProductId());
	    dto.setUserId(product.getUserId());
	    dto.setCategoryId(product.getCategoryId());
	    dto.setProductName(product.getProductName());
	    dto.setProductPrice(product.getProductPrice());
	    dto.setProductMrp(product.getProductMrp());
	    dto.setProductQuantity(product.getProductQuantity());
	    dto.setProductAvgRating(product.getProductAvgRating());
	    dto.setProductReviewsCount(product.getProductReviewsCount());
	    dto.setBrand(product.getBrand());
	    dto.setProductDescription(product.getProductDescription());
	    dto.setImageUrl(product.getImageUrl());
		return dto;
	}
	
	// Get request
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto){
		
		
		Product toCreate = dtoToEntity(dto);
		Product created = productService.createProduct(toCreate);
		return ResponseEntity.created(URI.create("/api/products/" + created.getProductId()))
				             .body(entityToDto(created));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductyId(@PathVariable("id") int ProductId) { 
		
		Product product= productService.getProductById(ProductId);
		return ResponseEntity.ok(entityToDto(product));		
	}
	
	@GetMapping
	public ResponseEntity<List<ProductDto>> getAllProducts() { 
		
		List<Product> products= productService.getAllProducts();
	    List<ProductDto> dtoList =products.stream().map(this::entityToDto).collect(Collectors.toList());

		return ResponseEntity.ok(dtoList);		
	}
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<ProductDto>> getProductByCategoryId(@PathVariable int categoryId) { 
		
	    List<Product> products = productService.getProductsByCategory(categoryId);
	    List<ProductDto> dtoList = products.stream().map(this::entityToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);		
	}
	
	@GetMapping("/seller/{userId}")
	public ResponseEntity<List<ProductDto>> getProductBySeller(@PathVariable int userId) { 
		
	    List<Product> products = productService.getProductsBySeller(userId);
	    List<ProductDto> dtoList = products.stream().map(this::entityToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);		
	}
	
	@GetMapping("/name")
	public ResponseEntity<List<ProductDto>> searchProductsByName(@RequestParam(value="name", required=false) String name) {		
		
		 List<Product> products = productService.searchProductsByName(name);
			 List<ProductDto> dtoList = products.stream().map(this::entityToDto).collect(Collectors.toList());
				return ResponseEntity.ok(dtoList);
		
		
	  		
	}
	@GetMapping("/brand")
	public ResponseEntity<List<ProductDto>> searchProductsByBrand(@RequestParam(value="Brand", required=false) String Brand) {		
		
		 List<Product> products = productService.searchProductsByBrand(Brand);
			 List<ProductDto> dtoList = products.stream().map(this::entityToDto).collect(Collectors.toList());
				return ResponseEntity.ok(dtoList);
		
		
	  		
	}
	
	// Put Request
		@PutMapping("/{id}")
		public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") int productId, @Valid @RequestBody ProductDto dto) {		
			
			 dto.setProduct_id(productId);
			 Product product = dtoToEntity(dto); 
			 Product updated= productService.updateProduct(product);
		     return ResponseEntity.ok(entityToDto(updated));		
		}
	
		
		
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> deleteProduct(@PathVariable("id") int productId) {
			productService.deleteProduct(productId);
			return ResponseEntity.noContent().build();
		}
		
		@GetMapping("/categories/{id}/products")
		public List<Map<String, Object>> getCategoryProducts(@PathVariable int id) {
		    return productService.fetchProductsByCategory(id);
		}
		
		@GetMapping("/seller/{id}/products")
		public List<Map<String, Object>> getSellerProducts(@PathVariable int id) {
		    return productService.fetchSellerProducts(id);
		}

	
	
}
	
	
	