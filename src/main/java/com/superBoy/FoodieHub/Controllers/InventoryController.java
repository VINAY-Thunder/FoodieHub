package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.IInventoryService;
import com.superBoy.FoodieHub.Request.DTOs.InventoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.InventoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory", description = "Stock levels and inventory tracking")
public class InventoryController {

    private final IInventoryService inventoryService;

    @Autowired
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Add inventory item", description = "Registers a new item in the inventory with an initial stock level.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventory item added"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<InventoryResponseDTO> addInventoryItem(@RequestBody @Valid InventoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventoryItem(dto));
    }

    @Operation(summary = "Get inventory item by ID", description = "Fetches a single inventory item by its unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory item found"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "Get all inventory items", description = "Returns a list of all inventory items and their current stock levels.")
    @ApiResponse(responseCode = "200", description = "All inventory items returned")
    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @Operation(summary = "Update inventory item", description = "Fully replaces an existing inventory item's details by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory item updated"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Long id, @RequestBody @Valid InventoryRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, dto));
    }

    @Operation(summary = "Delete inventory item", description = "Permanently removes an inventory item by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventory item deleted"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("Inventory item deleted successfully");
    }

    @Operation(summary = "Adjust stock level", description = "Applies a stock change (positive = restock, negative = deduction) to a specific inventory item.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated"),
        @ApiResponse(responseCode = "404", description = "Inventory item not found")
    })
    @PatchMapping("/{id}/stockChange")
    public ResponseEntity<InventoryResponseDTO> updateStock(@PathVariable Long id, @RequestParam Integer stockChange) {
        return ResponseEntity.ok(inventoryService.updateStock(id, stockChange));
    }
}
