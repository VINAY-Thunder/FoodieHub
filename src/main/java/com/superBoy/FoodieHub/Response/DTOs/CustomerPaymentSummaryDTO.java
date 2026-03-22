package com.superBoy.FoodieHub.Response.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.superBoy.FoodieHub.Enums.CustomerPaymentMethod;
import com.superBoy.FoodieHub.Enums.CustomerPaymentStatus;

public class CustomerPaymentSummaryDTO {

	    private Long paymentId;
	    private BigDecimal amount;
	    private CustomerPaymentStatus paymentStatus;
	    private LocalDateTime paymentDate;

	    // getters and setters...

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

	public CustomerPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(CustomerPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

}
