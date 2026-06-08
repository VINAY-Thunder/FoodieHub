package com.superBoy.FoodieHub.Controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.I_Service.IOrderItem;
import com.superBoy.FoodieHub.Response.DTOs.OrderItemResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/order-items")
@Tag(name = "Order Item", description = "Individual line items within an order")
public class OrderItemController {

	private final IOrderItem orderItemService;

	@Autowired
	public OrderItemController(IOrderItem orderItemService) {
		this.orderItemService = orderItemService;
	}

	@Operation(summary = "Get items by order ID", description = "Returns all line items that belong to the specified order.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Order items returned"),
		@ApiResponse(responseCode = "404", description = "Order not found")
	})
	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<OrderItemResponseDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
	}

	@Operation(summary = "Update item quantity", description = "Changes the quantity of a specific order item by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Quantity updated"),
		@ApiResponse(responseCode = "404", description = "Order item not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<OrderItemResponseDTO> updateOrderItemQuantity(@PathVariable Long id,
			@RequestParam BigDecimal quantity) {
		return ResponseEntity.ok(orderItemService.updateOrderItemQuantity(id, quantity));
	}

	@Operation(summary = "Remove item from order", description = "Deletes a line item from an order by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Item removed"),
		@ApiResponse(responseCode = "404", description = "Order item not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeOrderItem(@PathVariable Long id) {
		orderItemService.removeOrderItem(id);
		return ResponseEntity.ok("Item removed from order successfully");
	}
}
