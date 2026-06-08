package com.superBoy.FoodieHub.Request.DTOs;

import java.time.LocalDate;
import com.superBoy.FoodieHub.Enums.GenderIdentity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {

	@NotBlank(message = "Name is required")
	@Size(max = 100, message = "Name must not exceed 100 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email must be a valid email address")
	private String email;

	@NotBlank(message = "Phone is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	private String phone;

	@NotBlank(message = "Password is required")
	@Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
	private String password; // Stored as BCrypt hash

	@NotNull(message = "please enter correct Gender")
	private GenderIdentity gender;

	@NotNull(message = "please enter correct DOB")
	private LocalDate dateOfBirth;

	@Valid
	private CustomerAddressRequestDTO address;

	public CustomerAddressRequestDTO getAddress() {
		return address;
	}

	public void setAddress(CustomerAddressRequestDTO address) {
		this.address = address;
	}

	public CustomerRequestDTO() {
	}

	public String getName() {
		return name;
	}

	public GenderIdentity getGender() {
		return gender;
	}

	public void setGender(GenderIdentity gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
