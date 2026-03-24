package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;
import com.superBoy.FoodieHub.ExceptionHandling.InventoryNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.PurchaseOrderNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.SupplierNotException;
import com.superBoy.FoodieHub.ExceptionHandling.SupplierNotPaidException;
import com.superBoy.FoodieHub.I_Service.IPurchaseOrderService;
import com.superBoy.FoodieHub.Model.Inventory;
import com.superBoy.FoodieHub.Model.PurchaseItem;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Repository.InventoryRepository;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Repository.SupplierPaymentRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseOrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseItemResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseOrderResponseDTO;

@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final SupplierRepository supplierRepo;
    private final InventoryRepository inventoryRepo;
    private final SupplierPaymentRepository supplierPaymentRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository poRepo, SupplierRepository supplierRepo, 
                               InventoryRepository inventoryRepo, SupplierPaymentRepository supplierPaymentRepo, ModelMapper modelMapper) {
        this.poRepo = poRepo;
        this.supplierRepo = supplierRepo;
        this.inventoryRepo = inventoryRepo;
        this.supplierPaymentRepo = supplierPaymentRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO dto) {
        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new SupplierNotException("Supplier not found with ID: " + dto.getSupplierId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(supplier);
        po.setDeliveryDate(dto.getDeliveryDate());
        po.setStatus(PurchaseOrderStatus.PENDING);

        List<PurchaseItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (PurchaseItemRequestDTO itemDto : dto.getPurchaseItems()) {
            Inventory inventory = inventoryRepo.findById(itemDto.getInventoryId())
                    .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found with ID: " + itemDto.getInventoryId()));

            PurchaseItem item = new PurchaseItem();
            item.setPurchaseOrder(po);
            item.setInventory(inventory);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            
            BigDecimal subtotal = itemDto.getUnitPrice().multiply(new BigDecimal(itemDto.getQuantity()));
            item.setSubtotal(subtotal);
            totalAmount = totalAmount.add(subtotal);// future you can add like gst etc in totalAmmount
            items.add(item);
        }

        po.setPurchaseItems(items);
        po.setTotalAmount(totalAmount);

        PurchaseOrder saved = poRepo.save(po); // saved to DB
        return convertToResponseDTO(saved);
    }

    @Override
    public PurchaseOrderResponseDTO getPurchaseOrderById(Long id) {
        PurchaseOrder po = poRepo.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase Order not found with ID: " + id));
        return convertToResponseDTO(po);
    }

    @Override
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return poRepo.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO updatePurchaseOrderStatus(Long id, PurchaseOrderStatus status) {
        PurchaseOrder po = poRepo.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase Order not found with ID: " + id));
        
        PurchaseOrderStatus oldStatus = po.getStatus();//Old Status

        // New Safety Check: Ensure Supplier Payment is SUCCESS before changing status to RECEIVED
        if (status == PurchaseOrderStatus.RECEIVED && oldStatus != PurchaseOrderStatus.RECEIVED) {
            boolean isPaid = supplierPaymentRepo.existsByPurchaseOrderPurchaseOrderIdAndPaymentStatus(
                    id, com.superBoy.FoodieHub.Enums.SupplierPaymentStatus.SUCCESS);
            if (!isPaid) {
                throw new SupplierNotPaidException("Cannot receive order: Supplier payment has not been successfully completed.");
            }
        }

        po.setStatus(status); // now Setted by New Status

        // If received, update inventory stock , for each Inventory stock is updated 
        if (status == PurchaseOrderStatus.RECEIVED && oldStatus != PurchaseOrderStatus.RECEIVED) {
            for (PurchaseItem item : po.getPurchaseItems()) { //Many Purchase orders so only used ForEach loop
                Inventory inventory = item.getInventory();
                inventory.setCurrentStock(inventory.getCurrentStock() + item.getQuantity());
                inventoryRepo.save(inventory); // saved to DB
            }
        }

        PurchaseOrder updated = poRepo.save(po); // saved to DB
        return convertToResponseDTO(updated);
    }

	@Override
	public String removePurchaseOrderById(Long purchaseId) {
		PurchaseOrder PurchaseorderEntity = poRepo.findById(purchaseId)
				.orElseThrow(() -> new PurchaseOrderNotFoundException("PurchaseOrder Not Found with POId: "+purchaseId));
		 
		poRepo.deleteById(purchaseId);
		return "Successfully deleted PurchaseOrder with ID: "+purchaseId;
		
	}

	private PurchaseOrderResponseDTO convertToResponseDTO(PurchaseOrder po) {
		PurchaseOrderResponseDTO dto = modelMapper.map(po, PurchaseOrderResponseDTO.class);
		
		// Fill in missing properties for nested purchaseItems
		if (po.getPurchaseItems() != null) {
			List<PurchaseItemResponseDTO> itemsDtos = po.getPurchaseItems().stream().map(item -> {
				PurchaseItemResponseDTO itemDto = modelMapper.map(item, PurchaseItemResponseDTO.class);
				if (item.getInventory() != null) {
					itemDto.setInventoryId(item.getInventory().getInventoryId());
					itemDto.setItemName(item.getInventory().getItemName());
				}
				itemDto.setTotalPrice(item.getSubtotal());
				return itemDto;
			}).toList();
			dto.setPurchaseItems(itemsDtos);
		}
		
		return dto;
	}
}
