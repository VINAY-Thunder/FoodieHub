package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.SupplierAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierAddressResponseDTO;

public interface ISupplierAddressService {
    SupplierAddressResponseDTO addSupplierAddress(SupplierAddressRequestDTO dto);
    SupplierAddressResponseDTO updateSupplierAddress(Long addressId, SupplierAddressRequestDTO dto);
    SupplierAddressResponseDTO getSupplierAddressById(Long addressId);
    List<SupplierAddressResponseDTO> getAddressesBySupplierId(Long supplierId);
    void deleteSupplierAddress(Long addressId);
    List<SupplierAddressResponseDTO> getAllSupplierAddress();
}
