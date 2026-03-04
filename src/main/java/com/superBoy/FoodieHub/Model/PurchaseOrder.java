package com.superBoy.FoodieHub.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

@Entity
public class PurchaseOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_order_id")
	private Long purchaseOrderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	@CreationTimestamp
	@Column(name = "order_date", updatable = false)
	private LocalDateTime orderDate;

	@Column(name = "delivery_date")
	private LocalDateTime deliveryDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PurchaseOrderStatus status; // PENDING, RECEIVED, CANCELLED

	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	// Owner side of ManyToMany with Inventory via PURCHASE_ITEM junction table
//	@ManyToMany
//	@JoinTable(name = "PURCHASE_ITEM", joinColumns = @JoinColumn(name = "purchase_order_id"), inverseJoinColumns = @JoinColumn(name = "inventory_id"))
//	private List<Inventory> inventoryItems;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
	private List<PurchaseItem> purchaseItems;

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
	private List<SupplierPayment> payments;

	@PrePersist
	protected void onCreate() {
		this.status = PurchaseOrderStatus.PENDING; // auto set on create
	}

	public PurchaseOrder() {
	}

	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public PurchaseOrderStatus getStatus() {
		return status;
	}

	public void setStatus(PurchaseOrderStatus status) {
		this.status = status;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

//	public List<Inventory> getInventoryItems() {
//		return inventoryItems;
//	}
//
//	public void setInventoryItems(List<Inventory> inventoryItems) {
//		this.inventoryItems = inventoryItems;
//	}

	public List<PurchaseItem> getPurchaseItems() {
		return purchaseItems;
	}

	public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
		this.purchaseItems = purchaseItems;
	}

	public List<SupplierPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<SupplierPayment> payments) {
		this.payments = payments;
	}
}
