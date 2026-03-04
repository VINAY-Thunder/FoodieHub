package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

}
