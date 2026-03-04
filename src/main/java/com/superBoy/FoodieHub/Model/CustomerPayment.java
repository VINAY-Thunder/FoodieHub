package com.superBoy.FoodieHub.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.superBoy.FoodieHub.Enums.CustomerPaymentMethod;
import com.superBoy.FoodieHub.Enums.CustomerPaymentStatus;

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
@Table(name = "CUSTOMER_PAYMENT")
public class CustomerPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Orders order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(name = "amount")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private CustomerPaymentMethod paymentMethod; // CASH, CARD, UPI, ONLINE

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private CustomerPaymentStatus paymentStatus; // PENDING, COMPLETE, FAILED, REFUNDED

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "payment_date")
	private LocalDateTime paymentDate;

	public CustomerPayment() {
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public CustomerPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(CustomerPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public CustomerPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(CustomerPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}