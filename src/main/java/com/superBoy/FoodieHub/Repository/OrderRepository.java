package com.superBoy.FoodieHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superBoy.FoodieHub.Enums.OrderStatus;
import com.superBoy.FoodieHub.Enums.OrderType;
import com.superBoy.FoodieHub.Model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

	// DerivedMethods
	List<Orders> findByCustomerCustomerId(Long customerId);

	List<Orders> findByOrderStatus(OrderStatus orderStatus);

	List<Orders> findByOrderType(OrderType orderType);
}
