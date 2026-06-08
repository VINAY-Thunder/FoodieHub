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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/menu")
@Tag(name = "Menu", description = "Restaurant menu items — add, update, price, discount, availability")
public class MenuController {

	private final IMenuService menuService;

	@Autowired
	public MenuController(IMenuService menuService) {
		this.menuService = menuService;
	}

	@Operation(summary = "Add a menu item", description = "Creates a new menu item. Accepts multipart/form-data so an image file can be uploaded alongside the item details.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Menu item created successfully"),
		@ApiResponse(responseCode = "400", description = "Validation error")
	})
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<MenuResponseDTO> addMenu(@ModelAttribute @Valid MenuRequestDTO menuRequestDTO) {
		MenuResponseDTO response = menuService.addMenuItem(menuRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Get all menu items", description = "Returns the complete list of menu items regardless of status.")
	@ApiResponse(responseCode = "200", description = "All menu items returned")
	@GetMapping
	public ResponseEntity<List<MenuResponseDTO>> getAllMenu() {
		return ResponseEntity.ok(menuService.getAllMenuItems());
	}

	@Operation(summary = "Get menu item by ID", description = "Fetches a single menu item by its unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Menu item found"),
		@ApiResponse(responseCode = "404", description = "Menu item not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
		return ResponseEntity.ok(menuService.getMenuItemById(id));
	}

	@Operation(summary = "Get menu items by category", description = "Returns all menu items belonging to the given category ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Items returned"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<MenuResponseDTO>> getMenuItemsByCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(menuService.getMenuItemsByCategory(categoryId));
	}

	@Operation(summary = "Update a menu item", description = "Fully replaces an existing menu item. Accepts multipart/form-data including optional image replacement.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Menu item updated"),
		@ApiResponse(responseCode = "404", description = "Menu item not found")
	})
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id,
			@ModelAttribute @Valid MenuRequestDTO menuRequestDTO) {
		return ResponseEntity.ok(menuService.updateMenuItem(id, menuRequestDTO));
	}

	@Operation(summary = "Delete a menu item", description = "Permanently removes a menu item by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Menu item not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
		menuService.deleteMenuItem(id);
		return ResponseEntity.ok("Menu item deleted successfully with id: " + id);
	}

	@Operation(summary = "Update menu item availability status", description = "Changes the status (AVAILABLE / UNAVAILABLE / OUT_OF_STOCK) of a menu item.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Status updated"),
		@ApiResponse(responseCode = "404", description = "Menu item not found")
	})
	@PatchMapping("/{id}/status")
	public ResponseEntity<MenuResponseDTO> updateMenuStatus(@PathVariable Long id, @RequestParam MenuStatus status) {
		return ResponseEntity.ok(menuService.updateMenuStatus(id, status));
	}

	@Operation(summary = "Get available menu items", description = "Returns only menu items with status AVAILABLE.")
	@ApiResponse(responseCode = "200", description = "Available items returned")
	@GetMapping("/available")
	public ResponseEntity<List<MenuResponseDTO>> getAvailableMenu() {
		return ResponseEntity.ok(menuService.getAvailableMenuItems(MenuStatus.AVAILABLE));
	}

	@Operation(summary = "Apply discount to menu item", description = "Sets a discount percentage on a menu item price.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Discount applied"),
		@ApiResponse(responseCode = "404", description = "Menu item not found")
	})
	@PostMapping("/{id}/discount")
	public ResponseEntity<MenuResponseDTO> applyDiscount(@PathVariable Long id, @RequestParam BigDecimal discount) {
		return ResponseEntity.ok(menuService.applyDiscount(id, discount));
	}
}
