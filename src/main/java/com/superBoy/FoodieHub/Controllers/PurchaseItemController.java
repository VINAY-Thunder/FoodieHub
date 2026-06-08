package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.IPurchaseItemService;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/purchase-items")
@Tag(name = "Purchase Item", description = "Line items within a purchase order")
public class PurchaseItemController {

	private IPurchaseItemService purchaseItemService;

	@Autowired
	public PurchaseItemController(IPurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}

	@Operation(summary = "Create purchase item", description = "Adds a new line item to the specified purchase order.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Purchase item created"),
		@ApiResponse(responseCode = "400", description = "Validation error"),
		@ApiResponse(responseCode = "404", description = "Purchase order not found")
	})
	@PostMapping("/order/{purchaseOrderId}")
	public ResponseEntity<PurchaseItemResponseDTO> createPurchaseItem(@PathVariable Long purchaseOrderId,
			@Valid @RequestBody PurchaseItemRequestDTO requestDTO) {
		PurchaseItemResponseDTO item = purchaseItemService.createPurchaseItem(purchaseOrderId, requestDTO);
		return new ResponseEntity<>(item, HttpStatus.CREATED);
	}

	@Operation(summary = "Get items by purchase order", description = "Returns all line items belonging to the specified purchase order.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Purchase items returned"),
		@ApiResponse(responseCode = "404", description = "Purchase order not found")
	})
	@GetMapping("/order/{purchaseOrderId}")
	public ResponseEntity<List<PurchaseItemResponseDTO>> getPurchaseItemsByOrderId(@PathVariable Long purchaseOrderId) {
		return ResponseEntity.ok(purchaseItemService.getPurchaseItemsByOrderId(purchaseOrderId));
	}

	@Operation(summary = "Get all purchase items", description = "Returns every purchase item across all purchase orders.")
	@ApiResponse(responseCode = "200", description = "All purchase items returned")
	@GetMapping()
	public ResponseEntity<List<PurchaseItemResponseDTO>> getAllPurchaseItems() {
		return new ResponseEntity<>(purchaseItemService.getAllPurchaseItem(), HttpStatus.OK);
	}

	@Operation(summary = "Update purchase item", description = "Fully replaces a purchase item record by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Purchase item updated"),
		@ApiResponse(responseCode = "404", description = "Purchase item not found")
	})
	@PutMapping("/{purchaseItemId}")
	public ResponseEntity<PurchaseItemResponseDTO> updatePurchaseItem(@PathVariable Long purchaseItemId,
			@Valid @RequestBody PurchaseItemRequestDTO requestDTO) {
		return ResponseEntity.ok(purchaseItemService.updatePurchaseItem(purchaseItemId, requestDTO));
	}

	@Operation(summary = "Remove purchase item", description = "Permanently removes a line item from a purchase order.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Purchase item removed"),
		@ApiResponse(responseCode = "404", description = "Purchase item not found")
	})
	@DeleteMapping("/{purchaseItemId}")
	public ResponseEntity<String> removePurchaseItem(@PathVariable Long purchaseItemId) {
		purchaseItemService.removePurchaseItem(purchaseItemId);
		return ResponseEntity.ok("Purchase item removed successfully");
	}
}
