package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseOrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseOrderResponseDTO;
import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;

public interface IPurchaseOrderService {
    PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO);
    PurchaseOrderResponseDTO getPurchaseOrderById(Long purchaseOrderId);
    List<PurchaseOrderResponseDTO> getAllPurchaseOrders();
    PurchaseOrderResponseDTO updatePurchaseOrderStatus(Long purchaseOrderId, PurchaseOrderStatus status);
}
