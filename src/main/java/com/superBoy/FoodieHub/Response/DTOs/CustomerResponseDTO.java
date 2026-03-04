package com.superBoy.FoodieHub.Response.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.superBoy.FoodieHub.Enums.GenderIdentity;

public class CustomerResponseDTO {
	private Long customerId;
	private String name;
	private String email;
	private String phone;
	private LocalDateTime createdAt;
	private LocalDate dateOfBirth;
	private Integer loyaltyPoints;
	private Boolean isActive;
	private GenderIdentity gender;
	private List<CustomerAddressResponseDTO> addresses;

	public List<CustomerAddressResponseDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<CustomerAddressResponseDTO> addresses) {
		this.addresses = addresses;
	}

	public GenderIdentity getGender() {
		return gender;
	}

	public void setGender(GenderIdentity gender) {
		this.gender = gender;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(Integer loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
