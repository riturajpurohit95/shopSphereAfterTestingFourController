package com.ShopSphere.shop_sphere.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;


import com.ShopSphere.shop_sphere.model.Product;

public class ProductRowMapper implements RowMapper<Product> {
	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Product pr = new Product();
		pr.setProductId(rs.getInt("product_id"));
	    pr.setUserId(rs.getInt("user_id"));
	    pr.setCategoryId(rs.getInt("cateogry_id"));
	    pr.setProductPrice(rs.getBigDecimal("product_price"));
	    pr.setProductMrp(rs.getBigDecimal("product_mrp"));
	    pr.setProductQuantity(rs.getInt("quantity"));
	    pr.setProductAvgRating(rs.getBigDecimal("product_avg_rating"));
	    pr.setProductReviewsCount(rs.getInt("product_reviews_count"));
		
		return pr;
	}

}
