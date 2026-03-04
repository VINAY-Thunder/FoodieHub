package com.superBoy.FoodieHub.Response.DTOs;

import com.superBoy.FoodieHub.Enums.SupplierAddressType;

public class SupplierAddressResponseDTO {
	private Long addressId;
	private Long supplierId;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private SupplierAddressType addressType;

	public SupplierAddressResponseDTO() {
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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

	public SupplierAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(SupplierAddressType addressType) {
		this.addressType = addressType;
	}

}
