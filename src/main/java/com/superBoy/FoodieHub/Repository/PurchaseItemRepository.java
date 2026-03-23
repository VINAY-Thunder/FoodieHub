package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.PurchaseItem;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
	List<PurchaseItem> findByPurchaseOrderPurchaseOrderId(Long purchaseOrderId);
}
