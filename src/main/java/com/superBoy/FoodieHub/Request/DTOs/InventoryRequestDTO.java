package com.superBoy.FoodieHub.Request.DTOs;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class InventoryRequestDTO {

	@NotBlank(message = "Item name is required")
	@Size(max = 100, message = "Item name must not exceed 100 characters")
	private String itemName;

	@NotNull(message = "Current stock is required")
	@Min(value = 0, message = "Current stock cannot be negative")
	private Integer currentStock;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City must not exceed 100 characters")
	private String city;

	@NotBlank(message = "State is required")
	@Size(max = 100, message = "State must not exceed 100 characters")
	private String state;

	@NotNull(message = "Minimum stock is required")
	@Min(value = 0, message = "Minimum stock cannot be negative")
	private Integer minStock;

	@NotBlank(message = "Unit is required")
	@Pattern(regexp = "^(KG|LITERS)$", message = "Unit must be KG or LITERS")
	private String unit;

	private List<Long> menuIds; // optional - menus using this inventory item

	public InventoryRequestDTO() {
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
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

	public Integer getMinStock() {
		return minStock;
	}

	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}

}
