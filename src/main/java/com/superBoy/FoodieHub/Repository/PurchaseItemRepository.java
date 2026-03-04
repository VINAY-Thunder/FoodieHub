package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.PurchaseItem;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

}
