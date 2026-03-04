package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
