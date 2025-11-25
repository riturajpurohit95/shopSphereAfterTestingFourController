package com.ShopSphere.shop_sphere.controller;

import java.net.URI;
import java.util.List;
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

import com.ShopSphere.shop_sphere.dto.ConfirmUpiRequest;
import com.ShopSphere.shop_sphere.dto.PaymentDto;
import com.ShopSphere.shop_sphere.model.Payment;

import com.ShopSphere.shop_sphere.service.PaymentService;

import jakarta.validation.Valid;

@RestController // marking this class as a REST controller, automatically convert responses into JSON format
@RequestMapping("/api/payments") // Sets a base URL for all endpoints in this class
public class PaymentController {
	
	
	public PaymentController(PaymentService paymentService) {
		super();
		this.paymentService = paymentService;
	}


	private final PaymentService paymentService;
	
	
	@PostMapping
	public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentDto dto){
		
		
	
		PaymentDto created = paymentService.createPayment(dto);
		return ResponseEntity.created(URI.create("/api/payments/" + created.getPayment_id()))
				             .body(created);
		
	}
	//
	@PostMapping("/confirm-upi/{orderId}")
	public ResponseEntity<PaymentDto> confirmUpiPaymentOrder(@PathVariable int orderId, @RequestBody ConfirmUpiRequest request){
		
		
	
		PaymentDto updated = paymentService.confirmUpiPaymentOrder(
				orderId,
				request.getRazorpayPaymentId(),
				request.getRazorpaySignature(),
				request.getUpiVpa());
		return ResponseEntity.ok(updated);
		
	}
	
	//
	
	@GetMapping("/{id}")
	public ResponseEntity<PaymentDto> getPaymentById(@PathVariable("id") int PaymentId) { 
		
		PaymentDto payment= paymentService.getPaymentById(PaymentId);
		return ResponseEntity.ok(payment);		
	}
	
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<PaymentDto> getPaymentByOrderId(@PathVariable int orderId) { 
		
	    PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
		return ResponseEntity.ok(payment);		
	}
	
	
	
	
	// Put Request
		@PutMapping("/{id}/status")
		public ResponseEntity<PaymentDto> updatePaymentStatus(@PathVariable("id") int paymentId, @RequestParam("status") String status) {		
			
			PaymentDto updated= paymentService.updatePaymentStatus(paymentId, status);
		     return ResponseEntity.ok(updated);			
		}
	
		
		@PutMapping("/{id}/gateway")
		public ResponseEntity<PaymentDto> updateGatewayDetails(@PathVariable("id") int paymentId, @RequestParam("gatewayRef") String gatewayRef,@RequestParam("upiVpa") String upiVpa ,@RequestParam("responsePayload") String responsePayload) {		
			
			PaymentDto updated= paymentService.updateGatewayDetails(paymentId, gatewayRef, upiVpa, responsePayload);
		     return ResponseEntity.ok(updated);			
		}
		
		}
	