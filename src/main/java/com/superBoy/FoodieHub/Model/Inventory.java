package com.superBoy.FoodieHub.Model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "INVENTORY")
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_id")
	private Long inventoryId;

	@Column(name = "item_name", nullable = false)
	private String itemName;

	@Column(name = "current_stock")
	private Integer currentStock;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "min_stock")
	private Integer minStock;

	@Column(name = "unit")
	private String unit; // KG, LITERS

	@UpdateTimestamp
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;

	// ManyToMany with PurchaseOrder via PURCHASE_ITEM junction table
//	@ManyToMany(mappedBy = "inventoryItems")
//	private List<PurchaseOrder> purchaseOrders;

	/**
	 * Inverse side of ManyToMany with Menu via MENU_INVENTORY junction table. An
	 * inventory item (ingredient) can be used in multiple menu items.
	 */
	@JsonIgnore  // ← STOP Jackson from touching this field
	@ManyToMany(mappedBy = "inventoryItems")
	private List<Menu> menus;

	public Inventory() {
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

//	@OneToMany(mappedBy = "purchaseOrder") // newly added
//	private List<PurchaseItem> purchaseItems;

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
}
