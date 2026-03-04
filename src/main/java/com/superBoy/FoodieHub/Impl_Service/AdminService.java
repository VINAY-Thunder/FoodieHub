package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.superBoy.FoodieHub.ExceptionHandling.AdminNotFoundException;
import com.superBoy.FoodieHub.I_Service.IAdminService;
import com.superBoy.FoodieHub.Model.Admin;
import com.superBoy.FoodieHub.Repository.AdminRepository;
import com.superBoy.FoodieHub.Request.DTOs.AdminRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.AdminResponseDTO;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository adminRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminService(AdminRepository adminRepo, ModelMapper modelMapper) {
        this.adminRepo = adminRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public AdminResponseDTO addAdmin(AdminRequestDTO dto) {
        Admin admin = modelMapper.map(dto, Admin.class);
        Admin saved = adminRepo.save(admin);
        return modelMapper.map(saved, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + id));
        return modelMapper.map(admin, AdminResponseDTO.class);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepo.findAll().stream()
                .map(a -> modelMapper.map(a, AdminResponseDTO.class))
                .toList();
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO dto) {
        Admin existing = adminRepo.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + id));
        modelMapper.map(dto, existing);
        Admin updated = adminRepo.save(existing);
        return modelMapper.map(updated, AdminResponseDTO.class);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepo.existsById(id)) {
            throw new AdminNotFoundException("Admin not found with ID: " + id);
        }
        adminRepo.deleteById(id);
    }
}
