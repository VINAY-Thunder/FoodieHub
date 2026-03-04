package com.superBoy.FoodieHub.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);
}
