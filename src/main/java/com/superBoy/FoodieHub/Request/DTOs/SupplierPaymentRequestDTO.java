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

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	private BigDecimal amount;

	@NotNull(message = "Payment method is required")
	private SupplierPaymentMethod paymentMethod;

	public SupplierPaymentRequestDTO() {
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public SupplierPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(SupplierPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}