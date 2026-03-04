package com.superBoy.FoodieHub.I_Service;

import java.math.BigDecimal;
import java.util.List;

import com.superBoy.FoodieHub.Request.DTOs.OrderItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderItemResponseDTO;

public interface IOrderItem {

	OrderItemResponseDTO createOrderItem(OrderItemRequestDTO orderitemRequestDTO);

	// Called when customer wants to see all items in their order
    // Postman: GET /order-items/order/5   (orderId = 5)
    List<OrderItemResponseDTO> getOrderItemsByOrderId(Long orderId);

    // Called when customer wants to change quantity (e.g. 2 burgers → 3 burgers)
    // Postman: PUT /order-items/3   Body: { quantity: 3 }
    OrderItemResponseDTO updateOrderItemQuantity(Long orderItemId, BigDecimal newQuantity);

    // Called when customer removes one item from order
    // Postman: DELETE /order-items/3
    void removeOrderItem(Long orderItemId);





}
