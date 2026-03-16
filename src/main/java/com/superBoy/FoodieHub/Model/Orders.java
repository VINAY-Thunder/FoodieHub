package com.superBoy.FoodieHub.Model;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.superBoy.FoodieHub.Enums.OrderStatus;
import com.superBoy.FoodieHub.Enums.OrderType;

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
import jakarta.persistence.PreUpdate;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_type", nullable = false)
	private OrderType orderType; // DINE_IN, TAKEAWAY, DELIVERY

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	private BigDecimal totalAmount;

	@Column(name = "discount_amount")
	private BigDecimal discountAmount = BigDecimal.ZERO; // coupon/offer discount

	@Column(name = "tax_amount")
	private BigDecimal taxAmount = BigDecimal.ZERO; // GST

	@Column(name = "delivery_charge")
	private BigDecimal deliveryCharge = BigDecimal.ZERO; // 0 for dine-in/takeaway

	@Column(name = "final_amount")
	private BigDecimal finalAmount; // totalAmount - discount + tax + deliveryCharge

	@Column(name = "special_instructions", columnDefinition = "TEXT")
	private String specialInstructions; // "no onion", "extra spicy", "nut allergy"

	@Column(name = "coupon_code")
	private String couponCode; // applied coupon if any

	@Column(name = "table_number")
	private String tableNumber; // for DINE_IN orders

	@Column(name = "cancellation_reason")
	private String cancellationReason; // reason if CANCELLED

	@CreationTimestamp
	@Column(name = "order_date", updatable = false)
	private LocalDateTime orderDate;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// RELATIONSHIPS
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_address_id")
	private CustomerAddress customerAddress; // null for DINE_IN/TAKEAWAY

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CustomerPayment> payments = new ArrayList<>();

	public Orders() {
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(BigDecimal deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}

//	public List<Menu> getMenuItems() {
//		return menuItems;
//	}
//
//	public void setMenuItems(List<Menu> menuItems) {
//		this.menuItems = menuItems;
//	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<CustomerPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<CustomerPayment> payments) {
		this.payments = payments;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@PrePersist
	@PreUpdate
	public void calculateFinalAmount() {
		BigDecimal total = totalAmount != null ? totalAmount : BigDecimal.ZERO;
		BigDecimal disc = discountAmount != null ? discountAmount : BigDecimal.ZERO;
		BigDecimal tax = taxAmount != null ? taxAmount : BigDecimal.ZERO;
		BigDecimal delivery = deliveryCharge != null ? deliveryCharge : BigDecimal.ZERO;

		this.finalAmount = total.subtract(disc).add(tax).add(delivery);
	}

}