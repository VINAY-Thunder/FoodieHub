package com.superBoy.FoodieHub.Request.DTOs;

import java.math.BigDecimal;

import com.superBoy.FoodieHub.Enums.SupplierPaymentMethod;

import jakarta.validation.constraints.DecimalMin;

import jakarta.validation.constraints.NotNull;

public class SupplierPaymentRequestDTO {

	@NotNull(message = "Purchase order ID is required")
	private Long purchaseOrderId;

	@NotNull(message = "Supplier ID is required")
	private Long supplierId;

	// 1. SECURITY UPDATE: I intentionally removed the 'amount' field from this DTO!
	// The backend now securely reads the true amount directly from the Database so hackers cannot manipulate the price.

	@NotNull(message = "Payment method is required")
	private SupplierPaymentMethod paymentMethod;

	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;

	public SupplierPaymentRequestDTO() {
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

	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	// getters and setters for amount removed

	public SupplierPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(SupplierPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}