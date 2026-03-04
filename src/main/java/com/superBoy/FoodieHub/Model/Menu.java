package com.superBoy.FoodieHub.Model;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.superBoy.FoodieHub.Enums.MenuStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menuId;

	private String itemName;

	private String description;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;

	private BigDecimal  discountPercent; // e.g. 10.0 = 10% off

	private BigDecimal  discountedPrice; // hv to auto-calculated: price - (price * discountPercent/100)

	@Enumerated(EnumType.STRING)
	@Column(name = "is_available", nullable = false)
	private MenuStatus menuStatus;

	@Column(name = "is_veg", nullable = false)
	private Boolean isVeg;

	private Integer calories;

	private Integer serves;

	private String imageUrl;

	private Double rating;

	@Column(name = "total_ratings")
	private Integer totalRatings;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// RealtionShip
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>();

	/**
	 * ManyToMany with Inventory via MENU_INVENTORY junction table. A menu item can
	 * require multiple inventory items (ingredients). An inventory item can be used
	 * in multiple menu items. This is optional/informational.
	 */
	@ManyToMany
	@JoinTable(name = "MENU_INVENTORY", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "inventory_id"))
	private List<Inventory> inventoryItems;

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

	public BigDecimal  getPrice() {
		return price;
	}

	public void setPrice(BigDecimal  price) {
		this.price = price;
	}

	public BigDecimal  getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(BigDecimal  discountPercent) {
		this.discountPercent = discountPercent;
	}

	public BigDecimal  getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(BigDecimal  discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public MenuStatus getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(MenuStatus menuStatus) {
		this.menuStatus = menuStatus;
	}

	public Boolean getIsVeg() {
		return isVeg;
	}

	public void setIsVeg(Boolean isVeg) {
		this.isVeg = isVeg;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Integer getServes() {
		return serves;
	}

	public void setServes(Integer serves) {
		this.serves = serves;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getTotalRatings() {
		return totalRatings;
	}

	public void setTotalRatings(Integer totalRatings) {
		this.totalRatings = totalRatings;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

//	public List<Orders> getOrders() {
//		return orders;
//	}
//
//	public void setOrders(List<Orders> orders) {
//		this.orders = orders;
//	}

	public List<Inventory> getInventoryItems() {
		return inventoryItems;
	}

	public void setInventoryItems(List<Inventory> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Menu() {
		super();
		// TODO Auto-generated constructor stub
	}

}
