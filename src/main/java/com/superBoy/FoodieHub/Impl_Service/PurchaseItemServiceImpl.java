package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superBoy.FoodieHub.ExceptionHandling.InventoryNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.PurchaseItemNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.PurchaseOrderNotFoundException;
import com.superBoy.FoodieHub.I_Service.IPurchaseItemService;
import com.superBoy.FoodieHub.Model.Inventory;
import com.superBoy.FoodieHub.Model.PurchaseItem;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Repository.InventoryRepository;
import com.superBoy.FoodieHub.Repository.PurchaseItemRepository;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseItemResponseDTO;

@Service
public class PurchaseItemServiceImpl implements IPurchaseItemService {

	@Autowired
	private PurchaseItemRepository purchaseItemRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	@Transactional
	public PurchaseItemResponseDTO createPurchaseItem(Long purchaseOrderId, PurchaseItemRequestDTO requestDTO) {
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
				.orElseThrow(() -> new PurchaseOrderNotFoundException("PurchaseOrder not found with id: " + purchaseOrderId));

		Inventory inventory = inventoryRepository.findById(requestDTO.getInventoryId())
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + requestDTO.getInventoryId()));

		PurchaseItem purchaseItem = new PurchaseItem();
		purchaseItem.setPurchaseOrder(purchaseOrder);
		purchaseItem.setInventory(inventory);
		purchaseItem.setQuantity(requestDTO.getQuantity());
		purchaseItem.setUnitPrice(requestDTO.getUnitPrice());

		BigDecimal subtotal = requestDTO.getUnitPrice().multiply(new BigDecimal(requestDTO.getQuantity()));
		purchaseItem.setSubtotal(subtotal);

		purchaseItem = purchaseItemRepository.save(purchaseItem);
		
		updatePurchaseOrderTotal(purchaseOrder);

		return convertToResponseDTO(purchaseItem);
	}

	@Override
	public List<PurchaseItemResponseDTO> getPurchaseItemsByOrderId(Long purchaseOrderId) {
		List<PurchaseItem> items = purchaseItemRepository.findByPurchaseOrderPurchaseOrderId(purchaseOrderId);
		return items.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public PurchaseItemResponseDTO updatePurchaseItem(Long purchaseItemId, PurchaseItemRequestDTO requestDTO) {
		PurchaseItem purchaseItem = purchaseItemRepository.findById(purchaseItemId)
				.orElseThrow(() -> new PurchaseItemNotFoundException("PurchaseItem not found with id: " + purchaseItemId));

		if (!purchaseItem.getInventory().getInventoryId().equals(requestDTO.getInventoryId())) {
			Inventory newInventory = inventoryRepository.findById(requestDTO.getInventoryId())
					.orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + requestDTO.getInventoryId()));
			purchaseItem.setInventory(newInventory);
		}

		purchaseItem.setQuantity(requestDTO.getQuantity());
		purchaseItem.setUnitPrice(requestDTO.getUnitPrice());

		BigDecimal subtotal = requestDTO.getUnitPrice().multiply(new BigDecimal(requestDTO.getQuantity()));
		purchaseItem.setSubtotal(subtotal);

		purchaseItem = purchaseItemRepository.save(purchaseItem);
		
		updatePurchaseOrderTotal(purchaseItem.getPurchaseOrder());

		return convertToResponseDTO(purchaseItem);
	}

	@Override
	@Transactional
	public void removePurchaseItem(Long purchaseItemId) {
		PurchaseItem purchaseItem = purchaseItemRepository.findById(purchaseItemId)
				.orElseThrow(() -> new PurchaseItemNotFoundException("PurchaseItem not found with id: " + purchaseItemId));
		
		PurchaseOrder purchaseOrder = purchaseItem.getPurchaseOrder();
		purchaseItemRepository.delete(purchaseItem);
		
		// Wait for deletion to flush before calculating new total, or just compute minus
		purchaseItemRepository.flush();
		updatePurchaseOrderTotal(purchaseOrder);
	}

	private void updatePurchaseOrderTotal(PurchaseOrder purchaseOrder) {
		List<PurchaseItem> currentItems = purchaseItemRepository.findByPurchaseOrderPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
		BigDecimal newTotal = currentItems.stream()
				.map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		purchaseOrder.setTotalAmount(newTotal);
		purchaseOrderRepository.save(purchaseOrder);
	}

	private PurchaseItemResponseDTO convertToResponseDTO(PurchaseItem item) {
		PurchaseItemResponseDTO dto = new PurchaseItemResponseDTO();
		dto.setPurchaseItemId(item.getPurchaseItemId());
		dto.setPurchaseOrderId(item.getPurchaseOrder().getPurchaseOrderId());
		
		dto.setInventoryId(item.getInventory().getInventoryId());
		dto.setItemName(item.getInventory().getItemName());
		
		dto.setQuantity(item.getQuantity());
		dto.setUnitPrice(item.getUnitPrice());
		dto.setTotalPrice(item.getSubtotal());
		return dto;
	}

}
