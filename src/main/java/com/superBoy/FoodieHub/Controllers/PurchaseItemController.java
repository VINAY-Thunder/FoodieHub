package com.superBoy.FoodieHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superBoy.FoodieHub.I_Service.IPurchaseItemService;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseItemResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/purchase-items")
public class PurchaseItemController {

	@Autowired
	private IPurchaseItemService purchaseItemService;

	@PostMapping("/order/{purchaseOrderId}")
	public ResponseEntity<PurchaseItemResponseDTO> createPurchaseItem(
			@PathVariable Long purchaseOrderId,
			@Valid @RequestBody PurchaseItemRequestDTO requestDTO) {
		
		PurchaseItemResponseDTO item = purchaseItemService.createPurchaseItem(purchaseOrderId, requestDTO);
		return new ResponseEntity<>(item, HttpStatus.CREATED);
	}

	@GetMapping("/order/{purchaseOrderId}")
	public ResponseEntity<List<PurchaseItemResponseDTO>> getPurchaseItemsByOrderId(
			@PathVariable Long purchaseOrderId) {
		
		List<PurchaseItemResponseDTO> items = purchaseItemService.getPurchaseItemsByOrderId(purchaseOrderId);
		return ResponseEntity.ok(items);
	}

	@PutMapping("/{purchaseItemId}")
	public ResponseEntity<PurchaseItemResponseDTO> updatePurchaseItem(
			@PathVariable Long purchaseItemId,
			@Valid @RequestBody PurchaseItemRequestDTO requestDTO) {
		
		PurchaseItemResponseDTO item = purchaseItemService.updatePurchaseItem(purchaseItemId, requestDTO);
		return ResponseEntity.ok(item);
	}

	@DeleteMapping("/{purchaseItemId}")
	public ResponseEntity<String> removePurchaseItem(@PathVariable Long purchaseItemId) {
		purchaseItemService.removePurchaseItem(purchaseItemId);
		return ResponseEntity.ok("Purchase item removed successfully");
	}
}
