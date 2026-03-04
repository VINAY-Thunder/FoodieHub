package com.superBoy.FoodieHub.Controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.I_Service.IOrderItem;
import com.superBoy.FoodieHub.Response.DTOs.OrderItemResponseDTO;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

	private final IOrderItem orderItemService;

	@Autowired
	public OrderItemController(IOrderItem orderItemService) {
		this.orderItemService = orderItemService;
	}

	// 1. GET ALL ITEMS FOR AN ORDER
	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<OrderItemResponseDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
	}

	// 2. UPDATE ITEM QUANTITY
	@PutMapping("/{id}")
	public ResponseEntity<OrderItemResponseDTO> updateOrderItemQuantity(@PathVariable Long id,
			@RequestParam BigDecimal quantity) {
		return ResponseEntity.ok(orderItemService.updateOrderItemQuantity(id, quantity));
	}

	// 3. REMOVE ITEM FROM ORDER
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeOrderItem(@PathVariable Long id) {
		orderItemService.removeOrderItem(id);
		return ResponseEntity.ok("Item removed from order successfully");
	}
}
