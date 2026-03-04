package com.superBoy.FoodieHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.Enums.OrderStatus;
import com.superBoy.FoodieHub.Enums.OrderType;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.IOrderService;
import com.superBoy.FoodieHub.Request.DTOs.OrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final IOrderService orderService;

	@Autowired
	public OrderController(IOrderService orderService) {
		this.orderService = orderService;
	}

	// 1. CREATE ORDER
	@PostMapping
	public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO)
			throws CustomerNotFoundException {
		OrderResponseDTO response = orderService.createOrder(orderRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. GET ORDER BY ID
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	// 3. GET ORDERS BY CUSTOMER ID
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId)
			throws CustomerNotFoundException {
		return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
	}

	// 4. GET ALL ORDERS (ADMIN)
	@GetMapping
	public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	// 5. UPDATE ORDER STATUS (ADMIN)
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

	// 6. CANCEL ORDER
	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
		return ResponseEntity.ok(orderService.cancelOrder(id, reason));
	}

	// 7. GET ORDERS BY STATUS
	@GetMapping("/status")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.getOrdersByStatus(status));
	}

	// 8. GET ORDERS BY TYPE
	@GetMapping("/type")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByOrderType(@RequestParam OrderType type) {
		return ResponseEntity.ok(orderService.getOrdersByOrderType(type));
	}

	// 9. DELETE ORDER (ADMIN)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok("Order deleted successfully");
	}
}
