package com.superBoy.FoodieHub.Response.DTOs;

import com.superBoy.FoodieHub.Enums.CustomerAddressType;

//	Inside profile, you already know customer.
//	So you can remove customerId from address DTO for cleaner design.
//	Not mandatory, but better
public class CustomerAddressSummaryDTO {

	private Long addressId;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private CustomerAddressType addressType; // HOME / WORK

	// getters & setters

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
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

	public CustomerAddressSummaryDTO() {
		super();
	}
}