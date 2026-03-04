package com.superBoy.FoodieHub.Request.DTOs;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PurchaseOrderRequestDTO {

	@NotNull(message = "Supplier ID is required")
	private Long supplierId;

	@NotNull(message = "Delivery date is required")
	@Future(message = "Delivery date must be in the future")
	private LocalDateTime deliveryDate;

	@NotNull(message = "Purchase items are required")
	@Size(min = 1, message = "At least one purchase item is required")
	@Valid
	private List<PurchaseItemRequestDTO> purchaseItems;

	public PurchaseOrderRequestDTO() {
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public List<PurchaseItemRequestDTO> getPurchaseItems() {
		return purchaseItems;
	}

	public void setPurchaseItems(List<PurchaseItemRequestDTO> purchaseItems) {
		this.purchaseItems = purchaseItems;
	}

}