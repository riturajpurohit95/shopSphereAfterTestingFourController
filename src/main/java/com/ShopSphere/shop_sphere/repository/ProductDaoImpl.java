package com.ShopSphere.shop_sphere.repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ShopSphere.shop_sphere.model.OrderItem;
import com.ShopSphere.shop_sphere.model.Product;
import com.ShopSphere.shop_sphere.util.OrderItemRowMapper;
import com.ShopSphere.shop_sphere.util.ProductRowMapper;

@Repository
public class ProductDaoImpl implements ProductDao {
	

	    private final JdbcTemplate jdbcTemplate;
		
		public ProductDaoImpl(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}
		
		
		@Override
		    public int save(Product product) {    	
		    	
		    	 String sql = "INSERT INTO products (product_id,user_id,category_id,product_name,product_price,product_mrp,product_quantity,product_avg_rating,product_reviews_count,brand,description) " +
		                "VALUES (?, ?, ?, ?, ?,?,?,?,?,?)";
		         KeyHolder keyHolder = new GeneratedKeyHolder(); 
		         jdbcTemplate.update(connection -> {                   
		            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		            ps.setInt(1, product.getProductId());
		            ps.setInt(2, product.getUserId());
		            ps.setInt(3, product.getCategoryId());
		            ps.setInt(4, product.getProductQuantity());
		            ps.setBigDecimal(5, product.getProductPrice());
		            ps.setBigDecimal(6, product.getProductMrp());
		            ps.setBigDecimal(7, product.getProductAvgRating());
		            ps.setInt(8, product.getProductReviewsCount());
		            ps.setString(9, product.getBrand());
		            ps.setString(10,product.getDescription());
		            return ps;
		            }, keyHolder);   
		            Number key = keyHolder.getKey();
		            if (key != null) {
		            	product.setProductId(key.intValue());
		            }
		            return product.getProductId(); 		
		    }
			//find by order id
		
			@Override
			public Optional<Product> findById(int productId) {		
				String sql = "select * from products where product_id = ?";
				List<Product> list = jdbcTemplate.query(sql, new ProductRowMapper(),productId);
				if (list.isEmpty()) {
					return Optional.empty();
				}
				return Optional.of(list.get(0));		
			}
			 //find by order id
			
			@Override
			public List<Product> findAll() {		
				String sql = "select * from products ";
				return jdbcTemplate.query(sql, new ProductRowMapper());	
			}
	 //find by category id

			@Override
			public List<Product> findByCategory(int categoryId) {		
				String sql = "select * from products where category_id = ?";
			
						return jdbcTemplate.query(sql, new ProductRowMapper(),categoryId);
			}
			//find by seller
			@Override
			
			public List<Product> findBySeller(int userId) {		
				String sql = "select * from products where userId=? ";
				return jdbcTemplate.query(sql, new ProductRowMapper(),userId);	
			}
			//search by name
			public List<Product> searchByName(String name) {	
				if(name==null || name.trim().isEmpty()) {
					return findAll();
				}
				String sql = "select * from products where LOWER(name) LIKE ?";
				String input = "%" + name.toLowerCase()+ "%";
				return jdbcTemplate.query(sql, new ProductRowMapper(),input);
				
			}
			//update product
			public int update(Product product){
				String sql= "Update products set user_id = ?,category_id = ?,product_name = ?,product_price = ?,product_mrp = ?,product_quantity = ?,product_avg_rating = ?,product_reviews_count= ?, brand =?, description= ?  where product_id = ?) "+
	                  "VALUES (?, ?, ?, ?, ?,?,?,?)";
				
				return jdbcTemplate.update(sql,
						product.getProductName(),
						product.getUserId(),
						product.getCategoryId(),
						product.getProductPrice(),
						product.getProductMrp(),
						product.getProductQuantity(),
						product.getProductAvgRating(),
						product.getProductReviewsCount(),
				        product.getBrand(),
				        product.getDescription());
			}
			
			//delete product
			@Override
			public int deleteById(int productId) {
				
				String sql = "delete from products where product_id =?";
				return jdbcTemplate.update(sql,productId);
			}
			
			//search by Brand
			@Override
			public List<Product> searchByBrand(String Brand) {	
				if(Brand==null || Brand.trim().isEmpty()) {
					return findAll();
				}
				String sql = "select * from products where LOWER(Brand) LIKE ?";
				String pattern = "%" + Brand.toLowerCase()+ "%";
				return jdbcTemplate.query(sql, new ProductRowMapper(),pattern);
				
			}


			@Override
			public int getSellerIdByProductId(int productId) {
				String sql = "SELECT user_id FROM products WHERE product_id=?";
				return jdbcTemplate.queryForObject(sql, Integer.class, productId);
			}
		
}
