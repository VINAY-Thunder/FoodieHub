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

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	private BigDecimal amount;

	@NotNull(message = "Payment method is required")
	private CustomerPaymentMethod paymentMethod;

	public CustomerPaymentRequestDTO() {
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CustomerPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(CustomerPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
