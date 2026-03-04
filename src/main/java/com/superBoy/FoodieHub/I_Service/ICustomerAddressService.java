package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;

public interface ICustomerAddressService {
    CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO addressRequestDTO);
    List<CustomerAddressResponseDTO> getAddressesByCustomerId(Long customerId);
    void deleteAddress(Long addressId);
}
