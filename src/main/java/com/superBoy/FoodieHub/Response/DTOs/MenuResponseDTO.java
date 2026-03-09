package com.superBoy.FoodieHub.Response.DTOs;

import java.math.BigDecimal;
import java.util.List;

import com.superBoy.FoodieHub.Enums.MenuStatus;

public class MenuResponseDTO {
	private Long menuId;
	private Long categoryId;
	private String categoryName;
	private String itemName;
	private String description;
	private Double price;
	private MenuStatus menuStatus;
	private Boolean isVeg;
	private String imageUrl;
	private BigDecimal discountPercent; // show what discount was applied
	private BigDecimal discountedPrice; // show final calculated price
	private List<InventoryResponseDTO> inventoryItems;

	public MenuResponseDTO() {
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public MenuStatus getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(MenuStatus menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<InventoryResponseDTO> getInventoryItems() {
		return inventoryItems;
	}

	public void setInventoryItems(List<InventoryResponseDTO> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	public Boolean getIsVeg() {
		return isVeg;
	}

	public void setIsVeg(Boolean isVeg) {
		this.isVeg = isVeg;
	}

	public BigDecimal getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(BigDecimal discountPercent) {
		this.discountPercent = discountPercent;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

}