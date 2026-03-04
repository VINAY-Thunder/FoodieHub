package com.superBoy.FoodieHub.Update;

import org.springframework.web.multipart.MultipartFile;

import com.superBoy.FoodieHub.Enums.CategoryStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryUpdateDTO {

	@NotBlank(message = "Category name is required")
	@Size(min = 2, max = 100)
	private String categoryName;

	private String description;

	private Integer displayOrder;

	private CategoryStatus categoryStatus;

	private MultipartFile imageFile; // Added for image update support

	private String imageUrl; // Added to return the URL in the response

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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CategoryUpdateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
