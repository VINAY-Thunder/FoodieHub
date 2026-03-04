package com.superBoy.FoodieHub.Response.DTOs;

import com.superBoy.FoodieHub.Enums.CustomerAddressType;

public class CustomerAddressResponseDTO {
	private Long addressId;
	private Long customerId;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private CustomerAddressType addressType;

	public CustomerAddressResponseDTO() {
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public CustomerAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(CustomerAddressType addressType) {
		this.addressType = addressType;
	}

}
