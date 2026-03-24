package com.superBoy.FoodieHub.Request.DTOs;

import java.math.BigDecimal;
import com.superBoy.FoodieHub.Enums.CustomerPaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CustomerPaymentRequestDTO {

	@NotNull(message = "Order ID is required")
	private Long orderId;

	@NotNull(message = "Customer ID is required")
	private Long customerId;

	// 1. SECURITY UPDATE: I intentionally removed the 'amount' field from this DTO!
	// The backend now securely reads it directly from the Database so hackers cannot manipulate the price.

	@NotNull(message = "Payment method is required")
	private CustomerPaymentMethod paymentMethod;

	private String razorpayOrderId; // from RazorPay
	private String razorpayPaymentId; // from RazorPay after payment
	private String razorpaySignature; // for verification

	// getters and setters...

	public CustomerPaymentRequestDTO() {
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}

	public String getRazorpaySignature() {
		return razorpaySignature;
	}

	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	// getters and setters for amount removed

	public CustomerPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(CustomerPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
