package com.superBoy.FoodieHub.Response.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.superBoy.FoodieHub.Enums.CustomerPaymentMethod;

public class CustomerPaymentSummaryDTO {

	private Long paymentId;
	private BigDecimal amount;
	private CustomerPaymentMethod paymentStatus;
	private LocalDateTime paymentDate;

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CustomerPaymentMethod getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(CustomerPaymentMethod paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

}
