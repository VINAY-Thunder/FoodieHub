package com.superBoy.FoodieHub.Response.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryResponseDTO {
	private Long inventoryId;
	private String itemName;
	private Integer currentStock;
	private String city;
	private String state;
	private Integer minStock;
	private String unit;
	private LocalDateTime lastUpdated;
//	private List<MenuResponseDTO> menus;

	public InventoryResponseDTO() {
	}

	public Long getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		this.inventoryId = inventoryId;
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

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

//	public List<MenuResponseDTO> getMenus() {
//		return menus;
//	}
//
//	public void setMenus(List<MenuResponseDTO> menus) {
//		this.menus = menus;
//	}

}
