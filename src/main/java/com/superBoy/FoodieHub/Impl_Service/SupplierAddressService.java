package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.ExceptionHandling.SupplierAddressNotException;
import com.superBoy.FoodieHub.ExceptionHandling.SupplierNotException;
import com.superBoy.FoodieHub.I_Service.ISupplierAddressService;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Model.SupplierAddress;
import com.superBoy.FoodieHub.Repository.SupplierAddressRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Request.DTOs.SupplierAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierAddressResponseDTO;

@Service
public class SupplierAddressService implements ISupplierAddressService {

	private final SupplierAddressRepository addressRepo;
	private final SupplierRepository supplierRepo;
	private final ModelMapper modelMapper;

	@Autowired
	public SupplierAddressService(SupplierAddressRepository addressRepo, SupplierRepository supplierRepo,
			ModelMapper modelMapper) {
		this.addressRepo = addressRepo;
		this.supplierRepo = supplierRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public SupplierAddressResponseDTO addSupplierAddress(SupplierAddressRequestDTO dto) {
		Supplier supplier = supplierRepo.findById(dto.getSupplierId()).orElseThrow(
				() -> new SupplierAddressNotException("Supplier not found with ID: " + dto.getSupplierId()));

		SupplierAddress address = modelMapper.map(dto, SupplierAddress.class);
		address.setSupplier(supplier);
		SupplierAddress saved = addressRepo.save(address);
		return modelMapper.map(saved, SupplierAddressResponseDTO.class);
	}

	@Override
	public SupplierAddressResponseDTO updateSupplierAddress(Long addressId, SupplierAddressRequestDTO dto) {

		SupplierAddress addressEntity = addressRepo.findById(addressId)
				.orElseThrow(() -> new SupplierAddressNotException(
						"SupplierAddress is not found with SupplierAddressId: " + addressId));

		addressEntity.setAddressType(dto.getAddressType());
		addressEntity.setCity(dto.getCity());
		addressEntity.setState(dto.getState());
		addressEntity.setStreet(dto.getStreet());
		addressEntity.setZipCode(dto.getZipCode());

		addressRepo.save(addressEntity);
		SupplierAddressResponseDTO supplierAddressResponseDTO = modelMapper.map(addressEntity,
				SupplierAddressResponseDTO.class);

		return supplierAddressResponseDTO;
	}

	@Override
	public SupplierAddressResponseDTO getSupplierAddressById(Long addressId) {
		SupplierAddress address = addressRepo.findById(addressId)
				.orElseThrow(() -> new SupplierAddressNotException("Address not found with ID: " + addressId));
		return modelMapper.map(address, SupplierAddressResponseDTO.class);
	}

	@Override
	public List<SupplierAddressResponseDTO> getAddressesBySupplierId(Long supplierId) {
		// Checking if supplier exists
		supplierRepo.findById(supplierId)
				.orElseThrow(() -> new SupplierAddressNotException("Supplier not found with ID: " + supplierId));

		return addressRepo.findAll().stream().filter(addr -> addr.getSupplier().getSupplierId().equals(supplierId))
				.map(addr -> modelMapper.map(addr, SupplierAddressResponseDTO.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteSupplierAddress(Long addressId) {
		if (!addressRepo.existsById(addressId)) {
			throw new SupplierAddressNotException("Address not found with ID: " + addressId);
		}
		addressRepo.deleteById(addressId);
	}

	@Override
	public List<SupplierAddressResponseDTO> getAllSupplierAddress() {

		List<SupplierAddress> SupplierAddresses = addressRepo.findAll();
		List<SupplierAddressResponseDTO> addressResponseDTOs = SupplierAddresses.stream()
				.map(SupplierAddress -> modelMapper.map(SupplierAddress, SupplierAddressResponseDTO.class)).toList();

		return addressResponseDTOs;
	}
}
