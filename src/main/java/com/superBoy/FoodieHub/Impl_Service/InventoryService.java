package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.superBoy.FoodieHub.I_Service.IInventoryService;
import com.superBoy.FoodieHub.Model.Inventory;
import com.superBoy.FoodieHub.Repository.InventoryRepository;
import com.superBoy.FoodieHub.Request.DTOs.InventoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.InventoryResponseDTO;
import com.superBoy.FoodieHub.ExceptionHandling.InventoryNotFoundException;

@Service
public class InventoryService implements IInventoryService {

    private final InventoryRepository inventoryRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepo, ModelMapper modelMapper) {
        this.inventoryRepo = inventoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public InventoryResponseDTO addInventoryItem(InventoryRequestDTO dto) {
        Inventory item = modelMapper.map(dto, Inventory.class);
        Inventory saved = inventoryRepo.save(item);
        return modelMapper.map(saved, InventoryResponseDTO.class);
    }

    @Override
    public InventoryResponseDTO getInventoryById(Long id) {
        Inventory item = inventoryRepo.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found with ID: " + id));
        return modelMapper.map(item, InventoryResponseDTO.class);
    }

    @Override
    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepo.findAll().stream()
                .map(i -> modelMapper.map(i, InventoryResponseDTO.class))
                .toList();
    }

    @Override
    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO dto) {
        Inventory existing = inventoryRepo.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found with ID: " + id));
        modelMapper.map(dto, existing);
        Inventory updated = inventoryRepo.save(existing);
        return modelMapper.map(updated, InventoryResponseDTO.class);
    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepo.existsById(id)) {
            throw new InventoryNotFoundException("Inventory item not found with ID: " + id);
        }
        inventoryRepo.deleteById(id);
    }

    @Override
    public InventoryResponseDTO updateStock(Long id, Integer quantityChange) {
        Inventory existing = inventoryRepo.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found with ID: " + id));
        existing.setCurrentStock(existing.getCurrentStock() + quantityChange);
        Inventory updated = inventoryRepo.save(existing);
        return modelMapper.map(updated, InventoryResponseDTO.class);
    }
}
