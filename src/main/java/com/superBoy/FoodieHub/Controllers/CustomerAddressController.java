package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ICustomerAddressService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer-addresses")
public class CustomerAddressController {

    private final ICustomerAddressService addressService;

    @Autowired
    public CustomerAddressController(ICustomerAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<CustomerAddressResponseDTO> addAddress(@RequestBody @Valid CustomerAddressRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(dto));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerAddressResponseDTO>> getAddressesByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
