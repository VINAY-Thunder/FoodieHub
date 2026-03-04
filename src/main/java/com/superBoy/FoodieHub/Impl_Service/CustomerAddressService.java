package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.superBoy.FoodieHub.I_Service.ICustomerAddressService;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Repository.CustomerAddressRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;

@Service
public class CustomerAddressService implements ICustomerAddressService {

    private final CustomerAddressRepository addressRepo;
    private final CustomerRepository customerRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerAddressService(CustomerAddressRepository addressRepo, CustomerRepository customerRepo, ModelMapper modelMapper) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
        
        CustomerAddress address = modelMapper.map(dto, CustomerAddress.class);
        address.setCustomer(customer);
        CustomerAddress saved = addressRepo.save(address);
        return modelMapper.map(saved, CustomerAddressResponseDTO.class);
    }

    @Override
    public List<CustomerAddressResponseDTO> getAddressesByCustomerId(Long customerId) {
        return addressRepo.findByCustomerCustomerId(customerId).stream()
                .map(a -> modelMapper.map(a, CustomerAddressResponseDTO.class))
                .toList();
    }

    @Override
    public void deleteAddress(Long addressId) {
        if (!addressRepo.existsById(addressId)) {
            throw new RuntimeException("Address not found with ID: " + addressId);
        }
        addressRepo.deleteById(addressId);
    }
}
