package com.ShopSphere.shop_sphere.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Order;
import com.ShopSphere.shop_sphere.model.Product;
import com.ShopSphere.shop_sphere.repository.OrderDao;
import com.ShopSphere.shop_sphere.repository.ProductDao;

@Service
public class ProductServiceImpl implements ProductService {
	

	public ProductServiceImpl(ProductDao productDao) {
		super();
		this.productDao = productDao;
	}

    private final ProductDao productDao;
	
	
	
	@Override
	public Product createProduct(Product product) {
		if(product == null) {
			throw new IllegalArgumentException("Product must not be null");
		}
		int rows = productDao.save(product);
		if(rows <=0) {
			throw new RuntimeException("Create failed for Product");
		}
		return product;
	}
	
	
	
	@Override
	public Product getProductById(int productId) {
		return productDao.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found with id: "+productId));
	}
	@Override
	public List<Product> getAllProducts(){
		return productDao.findAll();
	}
	
	@Override
	public List<Product> getProductsByCategory(int categoryId){
		return productDao.findByCategory(categoryId);
	}
	
	@Override
	public List<Product> getProductsBySeller(int userId){
		return productDao.findBySeller(userId);
	}
	
	
	@Override
	public List<Product> searchProductsByName(String name){
		return productDao.searchByName(name);
	}
	public List<Product> searchProductsByBrand(String brand){
		return productDao.searchByBrand(brand);
	}

	@Override
	public Product updateProduct(Product product) {
	 if(product == null) {
		 throw new IllegalArgumentException("Product must not be null");
	 }
		getProductById(product.getProductId());
		
		int rows = productDao.update(product);
		if(rows <=0) {
			throw new RuntimeException("Update  failed for product Id: "+ product.getProductId());
		}
		
		return product;
	}
	
	
	
	@Override
	public void deleteProduct(int productId) {
		getProductById(productId);
		
		
		int rows = productDao.deleteById(productId);
		if(rows <=0) {
			throw new RuntimeException("Delete failed for product Id: "+ productId);
		}
		
	}
	

}
