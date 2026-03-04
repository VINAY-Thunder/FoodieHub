package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.superBoy.FoodieHub.I_Service.ISupplierService;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Request.DTOs.SupplierRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierResponseDTO;
import com.superBoy.FoodieHub.ExceptionHandling.CategoryNotFoundException; // Reusing or should create SupplierNotFoundException

@Service
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierService(SupplierRepository supplierRepo, ModelMapper modelMapper) {
        this.supplierRepo = supplierRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public SupplierResponseDTO addSupplier(SupplierRequestDTO dto) {
        Supplier supplier = modelMapper.map(dto, Supplier.class);
        Supplier saved = supplierRepo.save(supplier);
        return modelMapper.map(saved, SupplierResponseDTO.class);
    }

    @Override
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));
        return modelMapper.map(supplier, SupplierResponseDTO.class);
    }

    @Override
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepo.findAll().stream()
                .map(s -> modelMapper.map(s, SupplierResponseDTO.class))
                .toList();
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO dto) {
        Supplier existing = supplierRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));
        modelMapper.map(dto, existing);
        Supplier updated = supplierRepo.save(existing);
        return modelMapper.map(updated, SupplierResponseDTO.class);
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!supplierRepo.existsById(id)) {
            throw new RuntimeException("Supplier not found with ID: " + id);
        }
        supplierRepo.deleteById(id);
    }
}
