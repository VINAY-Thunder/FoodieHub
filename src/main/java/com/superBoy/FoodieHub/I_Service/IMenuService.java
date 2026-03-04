package com.superBoy.FoodieHub.I_Service;

import java.math.BigDecimal;
import java.util.List;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.Request.DTOs.MenuRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.MenuResponseDTO;

public interface IMenuService {

    // Create a new menu item
    MenuResponseDTO addMenuItem(MenuRequestDTO menuRequestDTO);

    // Get all menu items
    List<MenuResponseDTO> getAllMenuItems();

    // Get menu item by ID
    MenuResponseDTO getMenuItemById(Long menuId);

    // Get all menu items by category
    List<MenuResponseDTO> getMenuItemsByCategory(Long categoryId);

    // Update a menu item
    MenuResponseDTO updateMenuItem(Long menuId, MenuRequestDTO menuRequestDTO);

    // Delete a menu item
    void deleteMenuItem(Long menuId);

    // Toggle availability (AVAILABLE / UNAVAILABLE)
    MenuResponseDTO updateMenuStatus(Long menuId, MenuStatus status);

    // Get only available items (for customer-facing view)
    List<MenuResponseDTO> getAvailableMenuItems(MenuStatus menuStatus);

    // Apply discount to a menu item
    MenuResponseDTO applyDiscount(Long menuId, BigDecimal discountPercent);
}

