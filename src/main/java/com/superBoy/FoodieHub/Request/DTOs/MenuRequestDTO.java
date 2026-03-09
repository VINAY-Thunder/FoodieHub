package com.superBoy.FoodieHub.Request.DTOs;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.superBoy.FoodieHub.Enums.MenuStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MenuRequestDTO {

	@NotNull(message = "Category ID is required")
	private Long categoryId;

	@NotBlank(message = "Item name is required")
	@Size(max = 100, message = "Item name must not exceed 100 characters")
	private String itemName;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;

	private BigDecimal discountPercent; // user sends this (0-100)

	private MultipartFile imageFile; // ADD THIS - actual image upload

//	@NotNull(message = "Availability status is required")
//	private Boolean isAvailable;

	@NotNull(message = "Category type (Veg/Non-Veg) is required")
	private Boolean isVeg;

	@Size(max = 500, message = "Image URL must not exceed 500 characters")
	private String imageUrl;

	private List<Long> inventoryIds; // optional - ingredients

	private MenuStatus menuStatus;

	public MenuRequestDTO() {
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public MenuStatus getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(MenuStatus menuStatus) {
		this.menuStatus = menuStatus;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Long> getInventoryIds() {
		return inventoryIds;
	}

	public void setInventoryIds(List<Long> inventoryIds) {
		this.inventoryIds = inventoryIds;
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

}
