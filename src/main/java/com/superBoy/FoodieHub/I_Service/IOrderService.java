package com.superBoy.FoodieHub.I_Service;

import java.util.List;

import com.superBoy.FoodieHub.Enums.OrderStatus;
import com.superBoy.FoodieHub.Enums.OrderType;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.Request.DTOs.OrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderResponseDTO;

public interface IOrderService {
	
	// Create new order and //Handles all calculations inside		
	OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws CustomerNotFoundException;
	
	// Get order by ID
	OrderResponseDTO getOrderById(Long orderId);

	// Get all orders for a customer
	List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) throws CustomerNotFoundException;

	// Get all orders (for admin)
	List<OrderResponseDTO> getAllOrders();

	// Update order status
	OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus);

	// Cancel order
	OrderResponseDTO cancelOrder(Long orderId, String cancellationReason);

	// Get orders by status
	List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

	// Get orders by Enum order type (DINE_IN, TAKEAWAY, DELIVERY)
	List<OrderResponseDTO> getOrdersByOrderType(OrderType orderType);

	// Delete order (soft delete - for admin only)
	void deleteOrder(Long orderId);

}
