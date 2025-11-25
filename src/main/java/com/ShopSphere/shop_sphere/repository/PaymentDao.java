package com.ShopSphere.shop_sphere.repository;

import java.util.List;
import java.util.Optional;

import com.ShopSphere.shop_sphere.model.Payment;

public interface PaymentDao {
	
	int save(Payment payment);
	Optional<Payment> findById(int payment);
	Optional<Payment> findByOrderId(int orderId);
	int updateStatus(int paymentId, String status);
    int updateGatewayDetails(int paymentId, String gatewayRef, String upiVpa, String responsePayLoad);
}
