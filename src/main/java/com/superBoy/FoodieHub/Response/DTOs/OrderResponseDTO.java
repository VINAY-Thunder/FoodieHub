package com.superBoy.FoodieHub.Response.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
	private Long orderId;
	private Long customerId;
	private String customerName;
	private CustomerAddressResponseDTO customerAddress;;// One order = one address only
	private LocalDateTime orderDate;
	private String orderStatus;
	private BigDecimal totalAmount;
	private LocalDateTime deliveryDate;
	private String cancellationReason;
	private List<OrderItemResponseDTO> orderItems;

	public OrderResponseDTO() {
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public CustomerAddressResponseDTO getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddressResponseDTO customerAddress) {
		this.customerAddress = customerAddress;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public List<OrderItemResponseDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
		this.orderItems = orderItems;
	}

}
