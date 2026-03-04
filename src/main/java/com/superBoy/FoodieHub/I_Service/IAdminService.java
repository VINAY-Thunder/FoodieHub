package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.AdminRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.AdminResponseDTO;

public interface IAdminService {
    AdminResponseDTO addAdmin(AdminRequestDTO adminRequestDTO);
    AdminResponseDTO getAdminById(Long adminId);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO updateAdmin(Long adminId, AdminRequestDTO adminRequestDTO);
    void deleteAdmin(Long adminId);
}
