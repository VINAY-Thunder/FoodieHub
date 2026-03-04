package com.superBoy.FoodieHub.Model;

import com.superBoy.FoodieHub.Enums.CustomerAddressType;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class CustomerAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@NotBlank(message = "Street is required")
	@Column(name = "street", nullable = false)
	private String street;

	@NotBlank(message = "City is required")
	@Column(name = "city", nullable = false)
	private String city;

	@NotBlank(message = "State is required")
	@Column(name = "state", nullable = false)
	private String state;

	@NotBlank(message = "Zip code is required")
	@Pattern(regexp = "^[0-9]{6}$", message = "Zip code must be 6 digits")
	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Address type is required")
	@Column(name = "address_type", nullable = false)
	private CustomerAddressType addressType; // HOME, WORK, OTHER

	@ManyToOne(fetch = FetchType.LAZY) // using fetch.Lazy performance optimized
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	public CustomerAddress() {
	}

	public Long getAddressId() {
		return addressId;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public CustomerAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(CustomerAddressType addressType) {
		this.addressType = addressType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}