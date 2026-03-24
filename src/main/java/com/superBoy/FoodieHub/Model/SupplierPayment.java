package com.superBoy.FoodieHub.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.superBoy.FoodieHub.Enums.SupplierPaymentMethod;
import com.superBoy.FoodieHub.Enums.SupplierPaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SUPPLIER_PAYMENT")
public class SupplierPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id", nullable = false)
	private PurchaseOrder purchaseOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	@Column(name = "amount")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", length = 50)
	private SupplierPaymentMethod paymentMethod; // BANK_TRANSFER, CHEQUE, CASH

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private SupplierPaymentStatus paymentStatus; // PENDING, COMPLETED

	@Column(name = "transaction_id")
	private String transactionId;

	@CreationTimestamp
	@Column(name = "payment_date", updatable = false)
	private LocalDateTime paymentDate;

	public SupplierPayment() {
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
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

	public SupplierPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(SupplierPaymentStatus paymentStatus) {
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
