package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.superBoy.FoodieHub.I_Service.IPurchaseOrderService;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Model.PurchaseItem;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Model.Inventory;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Repository.InventoryRepository;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseOrderRequestDTO;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseOrderResponseDTO;
import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;

@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final SupplierRepository supplierRepo;
    private final InventoryRepository inventoryRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository poRepo, SupplierRepository supplierRepo, 
                               InventoryRepository inventoryRepo, ModelMapper modelMapper) {
        this.poRepo = poRepo;
        this.supplierRepo = supplierRepo;
        this.inventoryRepo = inventoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO dto) {
        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + dto.getSupplierId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(supplier);
        po.setDeliveryDate(dto.getDeliveryDate());
        po.setStatus(PurchaseOrderStatus.PENDING);

        List<PurchaseItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseItemRequestDTO itemDto : dto.getPurchaseItems()) {
            Inventory inventory = inventoryRepo.findById(itemDto.getInventoryId())
                    .orElseThrow(() -> new RuntimeException("Inventory item not found with ID: " + itemDto.getInventoryId()));

            PurchaseItem item = new PurchaseItem();
            item.setPurchaseOrder(po);
            item.setInventory(inventory);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            
            BigDecimal subtotal = itemDto.getUnitPrice().multiply(new BigDecimal(itemDto.getQuantity()));
            item.setSubtotal(subtotal);
            totalAmount = totalAmount.add(subtotal);
            items.add(item);
        }

        po.setPurchaseItems(items);
        po.setTotalAmount(totalAmount);

        PurchaseOrder saved = poRepo.save(po);
        return modelMapper.map(saved, PurchaseOrderResponseDTO.class);
    }

    @Override
    public PurchaseOrderResponseDTO getPurchaseOrderById(Long id) {
        PurchaseOrder po = poRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + id));
        return modelMapper.map(po, PurchaseOrderResponseDTO.class);
    }

    @Override
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return poRepo.findAll().stream()
                .map(po -> modelMapper.map(po, PurchaseOrderResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO updatePurchaseOrderStatus(Long id, PurchaseOrderStatus status) {
        PurchaseOrder po = poRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + id));
        
        PurchaseOrderStatus oldStatus = po.getStatus();
        po.setStatus(status);

        // If received, update inventory stock
        if (status == PurchaseOrderStatus.RECEIVED && oldStatus != PurchaseOrderStatus.RECEIVED) {
            for (PurchaseItem item : po.getPurchaseItems()) {
                Inventory inventory = item.getInventory();
                inventory.setCurrentStock(inventory.getCurrentStock() + item.getQuantity());
                inventoryRepo.save(inventory);
            }
        }

        PurchaseOrder updated = poRepo.save(po);
        return modelMapper.map(updated, PurchaseOrderResponseDTO.class);
    }
}
