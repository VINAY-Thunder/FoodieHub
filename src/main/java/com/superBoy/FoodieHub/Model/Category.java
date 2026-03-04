package com.superBoy.FoodieHub.Model;

import java.util.ArrayList;
import java.util.List;

import com.superBoy.FoodieHub.Enums.CategoryStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@NotBlank(message = "Category name is required")
	@Size(min = 2, max = 100)
	@Column(name = "category_name", nullable = false, unique = true)
	private String categoryName;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	private String imageUrl; // store S3 Bucket URL here

	private Integer displayOrder; // sort order in menu (e.g. Starters=1, Main=2)

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CategoryStatus categoryStatus;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Menu> menuItems = new ArrayList<>();

	public Category() {
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getDescription() {
		return description;
	}

	public List<Menu> getMenuItems() {
		return menuItems;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMenuItems(List<Menu> menuItems) {
		this.menuItems = menuItems;
	}

	public Category(@NotBlank(message = "Category name is required") @Size(min = 2, max = 100) String categoryName,
			String description, String imageUrl, Integer displayOrder, CategoryStatus categoryStatus,
			List<Menu> menuItems) {
		super();
		this.categoryName = categoryName;
		this.description = description;
		this.imageUrl = imageUrl;
		this.displayOrder = displayOrder;
		this.categoryStatus = categoryStatus;
		this.menuItems = menuItems;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", description=" + description
				+ ", imageUrl=" + imageUrl + ", displayOrder=" + displayOrder + ", categoryStatus=" + categoryStatus
				+ ", menuItems=" + menuItems + "]";
	}

}