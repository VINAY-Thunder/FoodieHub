package com.superBoy.FoodieHub.Request.DTOs;

import com.superBoy.FoodieHub.Enums.SupplierAddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupplierAddressRequestDTO {

	private Long supplierId;

	@NotBlank(message = "Street is required")
	@Size(max = 255, message = "Street must not exceed 255 characters")
	private String street;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City must not exceed 100 characters")
	private String city;
	
	@NotBlank(message = "State is required")
	@Size(max = 100, message = "State must not exceed 100 characters")
	private String state;

	@NotBlank(message = "Zip code is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "Zip code must be 6 digits")
	private String zipCode;

	@NotNull(message = "Address type must be WAREHOUSE, OFFICE or OTHER")
	private SupplierAddressType addressType;

	public SupplierAddressRequestDTO() {}

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