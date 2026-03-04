package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.SupplierRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierResponseDTO;

public interface ISupplierService {
    SupplierResponseDTO addSupplier(SupplierRequestDTO supplierRequestDTO);
    SupplierResponseDTO getSupplierById(Long supplierId);
    List<SupplierResponseDTO> getAllSuppliers();
    SupplierResponseDTO updateSupplier(Long supplierId, SupplierRequestDTO supplierRequestDTO);
    void deleteSupplier(Long supplierId);
}
