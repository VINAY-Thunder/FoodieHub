package com.superBoy.FoodieHub.Response.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.superBoy.FoodieHub.Enums.SupplierPaymentMethod;

public class SupplierPaymentResponseDTO {
	private Long paymentId;
	private Long purchaseOrderId;
	private Long supplierId;
	private String supplierName;
	private BigDecimal amount;
	private SupplierPaymentMethod paymentMethod;
	private com.superBoy.FoodieHub.Enums.SupplierPaymentStatus paymentStatus;
	private String transactionId;
	private LocalDateTime paymentDate;

	public SupplierPaymentResponseDTO() {
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public com.superBoy.FoodieHub.Enums.SupplierPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(com.superBoy.FoodieHub.Enums.SupplierPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

}
