package com.superBoy.FoodieHub.Request.DTOs;

import org.springframework.web.multipart.MultipartFile;

import com.superBoy.FoodieHub.Enums.CategoryStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryRequestDTO {

	@NotBlank(message = "Category name is required")
	@Size(max = 100, message = "Category name must not exceed 100 characters")
	private String categoryName;

	@Size(max = 500, message = "Description must not exceed 500 characters")
	private String description;

	private Integer displayOrder; // optional

	@NotNull(message = "Category status is required")
	private CategoryStatus categoryStatus;

	private MultipartFile imageFile; // Added for image upload support

	public CategoryRequestDTO() {
	}
	
	// getters & setters

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public CategoryStatus getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(CategoryStatus categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

}
