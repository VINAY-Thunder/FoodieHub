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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order", description = "Customer order lifecycle management")
public class OrderController {

	private final IOrderService orderService;

	@Autowired
	public OrderController(IOrderService orderService) {
		this.orderService = orderService;
	}


	@PostMapping
	public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO)
			throws CustomerNotFoundException {
		OrderResponseDTO response = orderService.createOrder(orderRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Get order by ID", description = "Fetches the full details of a single order.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Order found"),
		@ApiResponse(responseCode = "404", description = "Order not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@Operation(summary = "Get orders by customer", description = "Returns all orders placed by a specific customer.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Orders returned"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId)
			throws CustomerNotFoundException {
		return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
	}

	@Operation(summary = "Get all orders (Admin)", description = "Returns every order in the system. Intended for admin use.")
	@ApiResponse(responseCode = "200", description = "All orders returned")
	@GetMapping
	public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@Operation(summary = "Update order status (Admin)", description = "Changes the status of an order (e.g. PENDING → IN_PROGRESS → DELIVERED).")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Status updated"),
		@ApiResponse(responseCode = "404", description = "Order not found")
	})
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
	}

	@Operation(summary = "Cancel an order", description = "Cancels an existing order and records the cancellation reason.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Order cancelled"),
		@ApiResponse(responseCode = "404", description = "Order not found")
	})
	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
		return ResponseEntity.ok(orderService.cancelOrder(id, reason));
	}

	@Operation(summary = "Get orders by status", description = "Filters and returns orders matching a specific status.")
	@ApiResponse(responseCode = "200", description = "Filtered orders returned")
	@GetMapping("/status")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.getOrdersByStatus(status));
	}

	@Operation(summary = "Get orders by type", description = "Filters orders by order type (e.g. DINE_IN, TAKEAWAY, DELIVERY).")
	@ApiResponse(responseCode = "200", description = "Filtered orders returned")
	@GetMapping("/type")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByOrderType(@RequestParam OrderType type) {
		return ResponseEntity.ok(orderService.getOrdersByOrderType(type));
	}

	@Operation(summary = "Delete order (Admin)", description = "Permanently removes an order record by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Order deleted"),
		@ApiResponse(responseCode = "404", description = "Order not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok("Order deleted successfully with OrderId : " + id);
	}
}
