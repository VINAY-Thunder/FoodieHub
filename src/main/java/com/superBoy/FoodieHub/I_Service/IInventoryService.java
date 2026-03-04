package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.InventoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.InventoryResponseDTO;

public interface IInventoryService {
    InventoryResponseDTO addInventoryItem(InventoryRequestDTO inventoryRequestDTO);
    InventoryResponseDTO getInventoryById(Long inventoryId);
    List<InventoryResponseDTO> getAllInventory();
    InventoryResponseDTO updateInventory(Long inventoryId, InventoryRequestDTO inventoryRequestDTO);
    void deleteInventory(Long inventoryId);
    InventoryResponseDTO updateStock(Long inventoryId, Integer quantityChange);
}
