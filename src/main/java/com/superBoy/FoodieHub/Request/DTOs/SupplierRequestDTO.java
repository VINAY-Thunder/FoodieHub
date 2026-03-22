package com.superBoy.FoodieHub.Request.DTOs;

import com.superBoy.FoodieHub.Enums.ContactPerson;
import com.superBoy.FoodieHub.Enums.Supplier_Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupplierRequestDTO {

	@NotBlank(message = "Supplier name is required")
	@Size(max = 100, message = "Supplier name must not exceed 100 characters")
	private String supplierName;

	@NotNull(message = "Contact person is required")
	private ContactPerson contactPerson;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	private String phone;

	@NotNull(message = "Supplier status is required")
	private Supplier_Status status;

	// SupplierAddressDTO
	private SupplierAddressRequestDTO addresses;

	public SupplierRequestDTO() {
	}

	public Supplier_Status getStatus() {
		return status;
	}

	public void setStatus(Supplier_Status status) {
		this.status = status;
	}

	public SupplierAddressRequestDTO getAddresses() {
		return addresses;
	}

	public void setAddresses(SupplierAddressRequestDTO addresses) {
		this.addresses = addresses;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public ContactPerson getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
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

}
