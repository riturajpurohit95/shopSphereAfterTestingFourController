package com.ShopSphere.shop_sphere.service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ShopSphere.shop_sphere.dto.PaymentDto;
import com.ShopSphere.shop_sphere.exception.PaymentAlreadyCompletedException;
import com.ShopSphere.shop_sphere.exception.ResourceNotFoundException;
import com.ShopSphere.shop_sphere.model.Payment;
import com.ShopSphere.shop_sphere.repository.PaymentDao;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	public PaymentServiceImpl(PaymentDao paymentDao, RazorpayGatewayService razorpayGatewayService) {
		super();
		this.paymentDao = paymentDao;
		this.razorpayGatewayService = razorpayGatewayService;
	}

	private final PaymentDao paymentDao;
	private final RazorpayGatewayService razorpayGatewayService;
	
	
	private Payment dtoToModel(PaymentDto dto) {
	Payment payment = new Payment();
	payment.setPaymentId(dto.getPayment_id());
	payment.setOrderId(dto.getOrderId());
	payment.setUserId(dto.getUserId());
	payment.setAmount(dto.getAmount());
	payment.setCurrency(dto.getCurrency());
	payment.setPaymentMethod(dto.getPaymentMethod());
	payment.setCreatedAt(dto.getCreatedAt());
	payment.setStatus(dto.getStatus());
	payment.setGatewayRef(dto.getGatewayRef());
	payment.setUpiVpa(dto.getUpiVpa());
	payment.setResponsePayload(dto.getResponsePayLoad());
    
	return payment;
}


private PaymentDto modelToDto(Payment payment) {
	PaymentDto dto = new PaymentDto();
  dto.setPayment_id(payment.getPaymentId());
  dto.setOrderId(payment.getOrderId());
  dto.setUserId(payment.getUserId());
  dto.setAmount(payment.getAmount());
  dto.setCurrency(payment.getCurrency());
  dto.setPaymentMethod(payment.getPaymentMethod());
  dto.setStatus(payment.getStatus());
  dto.setGatewayRef(payment.getGatewayRef());
  dto.setUpiVpa(payment.getUpiVpa());
  dto.setResponsePayLoad(payment.getResponsePayload());
	return dto;
}

	@Override
	public PaymentDto createPayment(PaymentDto dto) {
		if(dto == null) {
			throw new IllegalArgumentException("PaymentDto must not be null");
		}
		Payment payment = dtoToModel(dto);
		payment.setCreatedAt(LocalDateTime.now());
		if(payment.getStatus() == null) {
			payment.setStatus("PENDING");
		}
		int rows = paymentDao.save(payment);
		if(rows <=0) {
			throw new RuntimeException("Create failed for payment of orderId: "+ payment.getOrderId());
		}
		
		//create razorpay order only for upi
		if("UPI".equalsIgnoreCase(payment.getPaymentMethod())) {
			BigDecimal amountInPaise = payment.getAmount().multiply(BigDecimal.valueOf(100));
			String receipt ="Order_" + payment.getOrderId();
			
			com.razorpay.Order razorpayOrder;
			try{
				razorpayOrder = razorpayGatewayService.createUpiOrder(receipt, amountInPaise, payment.getCurrency());
			}catch(Exception e) {
				throw new RuntimeException("Failed to create razorpay UPI order", e);
			}
			
			String razorpayOrderId = razorpayOrder.get("id").toString();
			payment.setGatewayRef(razorpayOrderId);
			payment.setResponsePayload(razorpayOrder.toString());
			
			int gwRows = paymentDao.updateGatewayDetails(payment.getPaymentId(), payment.getGatewayRef(),payment.getUpiVpa(), payment.getResponsePayload());
			
			if(gwRows <=0) {
				throw new RuntimeException("Failed to update gatewayDetails for paymentId: " + payment.getPaymentId());
			}
		}
			
		Optional<Payment> latest = paymentDao.findById(payment.getPaymentId());
		Payment finalPayment = latest.orElse(payment);
		
		return modelToDto(finalPayment);
		
		
	
	}
	
	@Override
	public PaymentDto getPaymentById(int paymentId) {
		
		Payment payment =  paymentDao.findById(paymentId).orElseThrow(()-> new RuntimeException("Payment not found with id: "+paymentId));
	return modelToDto(payment);
	}
	
	@Override
	public PaymentDto getPaymentByOrderId(int orderId){
		Payment payment = paymentDao.findByOrderId(orderId).orElseThrow(()-> new RuntimeException("Payment not found with id: "+ orderId));
		return modelToDto(payment);
	}
	

	@Override
	public PaymentDto updatePaymentStatus(int paymentId, String status) {
	  
		//getPaymentById(paymentId);
		PaymentDto existing = getPaymentById(paymentId);
		if("PAID".equalsIgnoreCase(existing.getStatus())) {
			throw new PaymentAlreadyCompletedException(paymentId);
		}
		
		int rows = paymentDao.updateStatus(paymentId, status);
		if(rows <=0) {
			throw new RuntimeException("Update status failed for Payment Id: "+ paymentId);
		}
		
		return getPaymentById(paymentId);
	}
	
	
		@Override
		public PaymentDto confirmUpiPaymentOrder(int orderId, String razorpayPaymentId, String razorpaySignature, String upiVpa){
			Payment payment = paymentDao.findByOrderId(orderId).orElseThrow(()-> new RuntimeException("No Payment stored with order id: "+orderId));
			;
		
		
		 String razorpayOrderId = payment.getGatewayRef();
		 if(razorpayOrderId == null) {
			 throw new RuntimeException("No razorpay order id is stored for payment id: "+ payment.getPaymentId());
		 }
			
			
			boolean valid = razorpayGatewayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
			
			
			if(!valid) {
				throw new RuntimeException("Invalid razorpay signatures for payment id: "+payment.getPaymentId());
			}
			
			//update details again after signature match
			int gwRows = paymentDao.updateGatewayDetails(payment.getPaymentId(), payment.getGatewayRef(),upiVpa, payment.getResponsePayload());
			
			if(gwRows<=0) {
				throw new RuntimeException("Failed to update gateway details for id: "+ payment.getPaymentId());
			}
			
			//update status to paid
			int statusRows = paymentDao.updateStatus(payment.getPaymentId(),"PAID"); 
					if(statusRows <=0) {
				throw new RuntimeException("Failed to update payment status for id: "+ payment.getPaymentId());
			}
					return getPaymentById(payment.getPaymentId());
		}
			
	@Override
	public PaymentDto updateGatewayDetails(int paymentId, String gatewayRef, String upiVpa, String razorPayload) {
	
		
		
		int rows = paymentDao.updateGatewayDetails(paymentId, gatewayRef, upiVpa, razorPayload);
		if(rows <=0) {
			throw new RuntimeException("Failed to update gateway details for id: "+ paymentId);
		}
		
		return getPaymentById(paymentId);
	}

	@Override
	public List<Map<String, Object>> getPaymentDetails(int userId) {
        return paymentDao.getPaymentDetails(userId);
    }
	
	
	

}
