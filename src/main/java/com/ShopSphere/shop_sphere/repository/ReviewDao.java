package com.ShopSphere.shop_sphere.repository;

import java.util.List;

import com.ShopSphere.shop_sphere.model.Review;

public interface ReviewDao {
	
	int save(Review review);
	List<Review> findByProduct(int productId);
	List<Review> findByUser(int userId);
	int updateStatus(int reviewIs, String status);

}
