package com.superBoy.FoodieHub.I_Service;

import java.util.List;

import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseItemResponseDTO;

public interface IPurchaseItemService {

	PurchaseItemResponseDTO createPurchaseItem(Long purchaseOrderId, PurchaseItemRequestDTO purchaseItemRequestDTO);

	List<PurchaseItemResponseDTO> getPurchaseItemsByOrderId(Long purchaseOrderId);

	PurchaseItemResponseDTO updatePurchaseItem(Long purchaseItemId, PurchaseItemRequestDTO purchaseItemRequestDTO);

	void removePurchaseItem(Long purchaseItemId);
}
