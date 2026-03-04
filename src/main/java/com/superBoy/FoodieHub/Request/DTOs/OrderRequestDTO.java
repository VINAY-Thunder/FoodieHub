package com.superBoy.FoodieHub.Request.DTOs;

import java.util.List;
import com.superBoy.FoodieHub.Enums.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderRequestDTO {

	@NotNull(message = "Customer ID is required")
	private Long customerId;

	@NotNull(message = "Order type is required")
	private OrderType orderType; // DELIVERY, DINE_IN, TAKEAWAY

	private Long customerAddressId;

	// Required only for DINE_IN (validated in service)
	private String tableNumber;

	private String specialInstructions; // optional: "no onion"

	private String couponCode; // optional

	@NotNull(message = "Order items are required")
	@Size(min = 1, message = "At least one order item is required")
	@Valid
	private List<OrderItemRequestDTO> orderItems;

	public OrderRequestDTO() {
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Long getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(Long customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
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

	public List<OrderItemRequestDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemRequestDTO> orderItems) {
		this.orderItems = orderItems;
	}
}