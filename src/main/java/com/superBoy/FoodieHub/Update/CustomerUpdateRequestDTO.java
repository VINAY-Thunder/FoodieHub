package com.superBoy.FoodieHub.Update;

import java.time.LocalDate;

import com.superBoy.FoodieHub.Enums.GenderIdentity;

public class CustomerUpdateRequestDTO {

	private String name;
	private String email;
	private String phone;
	private String password;
	private GenderIdentity gender;
	private LocalDate dateOfBirth;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
