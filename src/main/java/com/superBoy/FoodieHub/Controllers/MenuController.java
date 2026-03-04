package com.superBoy.FoodieHub.Controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.I_Service.IMenuService;
import com.superBoy.FoodieHub.Request.DTOs.MenuRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.MenuResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/menu")
public class MenuController {

	private final IMenuService menuService;

	@Autowired
	public MenuController(IMenuService menuService) {
		this.menuService = menuService;
	}

	// 1. ADD MENU ITEM
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<MenuResponseDTO> addMenu(@ModelAttribute @Valid MenuRequestDTO menuRequestDTO) {
		MenuResponseDTO response = menuService.addMenuItem(menuRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. GET ALL MENU ITEMS
	@GetMapping
	public ResponseEntity<List<MenuResponseDTO>> getAllMenu() {
		return ResponseEntity.ok(menuService.getAllMenuItems());
	}

	// 3. GET MENU ITEM BY ID
	@GetMapping("/{id}")
	public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
		return ResponseEntity.ok(menuService.getMenuItemById(id));
	}

	// 4. GET MENU ITEMS BY CATEGORY
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<MenuResponseDTO>> getMenuItemsByCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(menuService.getMenuItemsByCategory(categoryId));
	}

	// 5. UPDATE MENU ITEM
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id,
			@ModelAttribute @Valid MenuRequestDTO menuRequestDTO) {
		return ResponseEntity.ok(menuService.updateMenuItem(id, menuRequestDTO));
	}

	// 6. DELETE MENU ITEM
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
		menuService.deleteMenuItem(id);
		return ResponseEntity.ok("Menu item deleted successfully with id: "+id);
	}

	// 7. UPDATE MENU STATUS
	@PatchMapping("/{id}/status")
	public ResponseEntity<MenuResponseDTO> updateMenuStatus(@PathVariable Long id, @RequestParam MenuStatus status) {
		return ResponseEntity.ok(menuService.updateMenuStatus(id, status));
	}

	// 8. GET AVAILABLE MENU ITEMS
	@GetMapping("/available")
	public ResponseEntity<List<MenuResponseDTO>> getAvailableMenu() {
		return ResponseEntity.ok(menuService.getAvailableMenuItems(MenuStatus.AVAILABLE));
	}

	// 9. APPLY DISCOUNT
	@PostMapping("/{id}/discount")
	public ResponseEntity<MenuResponseDTO> applyDiscount(@PathVariable Long id, @RequestParam BigDecimal discount) {
		return ResponseEntity.ok(menuService.applyDiscount(id, discount));
	}

}
