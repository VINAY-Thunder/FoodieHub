package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.IInventoryService;
import com.superBoy.FoodieHub.Request.DTOs.InventoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.InventoryResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final IInventoryService inventoryService;

    @Autowired
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> addInventoryItem(@RequestBody @Valid InventoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventoryItem(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Long id, @RequestBody @Valid InventoryRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("Inventory item deleted successfully");
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<InventoryResponseDTO> updateStock(@PathVariable Long id, @RequestParam Integer change) {
        return ResponseEntity.ok(inventoryService.updateStock(id, change));
    }
}
