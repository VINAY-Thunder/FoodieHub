package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.IPurchaseOrderService;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseOrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseOrderResponseDTO;
import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final IPurchaseOrderService poService;

    @Autowired
    public PurchaseOrderController(IPurchaseOrderService poService) {
        this.poService = poService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poService.createPurchaseOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDTO> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(poService.getPurchaseOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllPurchaseOrders() {
        return ResponseEntity.ok(poService.getAllPurchaseOrders());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseOrderResponseDTO> updateStatus(@PathVariable Long id, @RequestParam PurchaseOrderStatus status) {
        return ResponseEntity.ok(poService.updatePurchaseOrderStatus(id, status));
    }
}
