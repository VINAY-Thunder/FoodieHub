package com.superBoy.FoodieHub.Response.DTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.superBoy.FoodieHub.Enums.ContactPerson;
import com.superBoy.FoodieHub.Enums.Supplier_Status;

public class SupplierResponseDTO {
	private Long supplierId;
	private String supplierName;
	private ContactPerson contactPerson;
	private String email;
	private String phone;
	private com.superBoy.FoodieHub.Enums.Supplier_Status status;
	private LocalDateTime createdAt;
	private List<SupplierAddressResponseDTO> addresses;

	
	public SupplierResponseDTO() {
	}

	public com.superBoy.FoodieHub.Enums.Supplier_Status getStatus() {
		return status;
	}

	public void setStatus(Supplier_Status status) {
		this.status = status;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<SupplierAddressResponseDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<SupplierAddressResponseDTO> addresses) {
		this.addresses = addresses;
	}

}