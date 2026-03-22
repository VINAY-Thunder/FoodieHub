package com.superBoy.FoodieHub.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "SUPPLIER")
public class Supplier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Long supplierId;

	@Column(name = "supplier_name", nullable = false)
	private String supplierName;

	@Enumerated(EnumType.STRING)
	@Column(name = "contact_person")
	private com.superBoy.FoodieHub.Enums.ContactPerson contactPerson;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone")
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private com.superBoy.FoodieHub.Enums.Supplier_Status status;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
	private List<SupplierAddress> addresses;

	@OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
	private List<PurchaseOrder> purchaseOrders;

	public Supplier() {
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

	public com.superBoy.FoodieHub.Enums.ContactPerson getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(com.superBoy.FoodieHub.Enums.ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}

	public com.superBoy.FoodieHub.Enums.Supplier_Status getStatus() {
		return status;
	}

	public void setStatus(com.superBoy.FoodieHub.Enums.Supplier_Status status) {
		this.status = status;
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

	public List<SupplierAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<SupplierAddress> addresses) {
		this.addresses = addresses;
	}

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
}
