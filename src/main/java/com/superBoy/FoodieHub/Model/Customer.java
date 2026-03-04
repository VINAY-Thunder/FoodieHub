package com.superBoy.FoodieHub.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.superBoy.FoodieHub.Enums.GenderIdentity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@NotBlank(message = "Phone is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	@Column(name = "phone", nullable = false)
	private String phone;

	private String password;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Enumerated(EnumType.STRING)
	private GenderIdentity gender; // MALE, FEMALE, OTHER

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true; // false = account blocked by admin

	@Column(name = "loyalty_points", nullable = false)
	private Integer loyaltyPoints = 0;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	// RELATIONSHIPS

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<CustomerAddress> addresses = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Orders> orders = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CustomerPayment> payments = new ArrayList<>();

	// CONSTRUCTORS
	public Customer() {
	}

	// GETTERS

	public Long getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public GenderIdentity getGender() {
		return gender;
	}

	public void setGender(GenderIdentity gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public Integer getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public List<CustomerAddress> getAddresses() {
		return addresses;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public List<CustomerPayment> getPayments() {
		return payments;
	}

	// SETTERS

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setLoyaltyPoints(Integer loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setAddresses(List<CustomerAddress> addresses) {
		this.addresses = addresses;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public void setPayments(List<CustomerPayment> payments) {
		this.payments = payments;
	}

}