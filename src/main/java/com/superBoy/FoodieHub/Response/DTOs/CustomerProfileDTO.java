package com.superBoy.FoodieHub.Response.DTOs;

import java.util.List;

public class CustomerProfileDTO {

	private Long id;
	private String name;
	private String email;
	private String phoneNumber;

	List<OrderSummaryDTO> orderSummaryDTOs;

	List<CustomerPaymentSummaryDTO> customerPaymentSummaryDTOs;

	List<CustomerAddressSummaryDTO> addressSummaryDTOs;

	// getters and setters
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<OrderSummaryDTO> getOrderSummaryDTOs() {
		return orderSummaryDTOs;
	}

	public void setOrderSummaryDTOs(List<OrderSummaryDTO> orderSummaryDTOs) {
		this.orderSummaryDTOs = orderSummaryDTOs;
	}

	public List<CustomerPaymentSummaryDTO> getCustomerPaymentSummaryDTOs() {
		return customerPaymentSummaryDTOs;
	}

	public void setCustomerPaymentSummaryDTOs(List<CustomerPaymentSummaryDTO> customerPaymentSummaryDTOs) {
		this.customerPaymentSummaryDTOs = customerPaymentSummaryDTOs;
	}

	public List<CustomerAddressSummaryDTO> getAddressSummaryDTOs() {
		return addressSummaryDTOs;
	}

	public void setAddressSummaryDTOs(List<CustomerAddressSummaryDTO> addressSummaryDTOs) {
		this.addressSummaryDTOs = addressSummaryDTOs;
	}

	public CustomerProfileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
