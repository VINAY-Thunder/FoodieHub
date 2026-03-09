package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.ExceptionHandling.CategoryNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.DiscountNotApplicable;
import com.superBoy.FoodieHub.ExceptionHandling.EmptyMenuListException;
import com.superBoy.FoodieHub.ExceptionHandling.InventoryNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.MenuNotFoundException;
import com.superBoy.FoodieHub.I_Service.IMenuService;
import com.superBoy.FoodieHub.Model.Category;
import com.superBoy.FoodieHub.Model.Inventory;
import com.superBoy.FoodieHub.Model.Menu;
import com.superBoy.FoodieHub.Repository.CategoryRepository;
import com.superBoy.FoodieHub.Repository.InventoryRepository;
import com.superBoy.FoodieHub.Repository.MenuRepository;
import com.superBoy.FoodieHub.Request.DTOs.MenuRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.MenuResponseDTO;

@Service
public class MenuService implements IMenuService {

	private MenuRepository menurepo;
	private CategoryRepository categoryrepo;
	private ModelMapper modelMapper;
	private InventoryRepository inventoryrepo;
	private S3Service s3Service;

	@Autowired
	public MenuService(MenuRepository menurepo, CategoryRepository categoryrepo, ModelMapper modelMapper,
			InventoryRepository inventoryrepo, S3Service s3Service) {
		super();
		this.menurepo = menurepo;
		this.categoryrepo = categoryrepo;
		this.modelMapper = modelMapper;
		this.inventoryrepo = inventoryrepo;
		this.s3Service = s3Service;
	}

	// 1. ADD MENU ITEM (with image upload)
	// ─────────────────────────────────────────────
	@Override
	public MenuResponseDTO addMenuItem(MenuRequestDTO dto) {

		Category category = categoryrepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + dto.getCategoryId()));

		Menu menu = modelMapper.map(dto, Menu.class);

		menu.setCategory(category);
		menu.setMenuStatus(dto.getMenuStatus());
		menu.setDiscountPercent(BigDecimal.ZERO);
		menu.setDiscountedPrice(menu.getPrice());

		if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
			String imageUrl = s3Service.uploadFile(dto.getImageFile().getOriginalFilename(), dto.getImageFile());
			menu.setImageUrl(imageUrl);
		}

		if (dto.getInventoryIds() != null && !dto.getInventoryIds().isEmpty()) {
			List<Inventory> inventoryItems = inventoryrepo.findAllById(dto.getInventoryIds());

			// ✅ NEW: check if all inventory IDs actually exist
			if (inventoryItems.size() != dto.getInventoryIds().size()) {
				throw new InventoryNotFoundException("One or more Inventory IDs not found");
			}

			menu.setInventoryItems(inventoryItems);
		}

		Menu savedMenu = menurepo.save(menu);
		return modelMapper.map(savedMenu, MenuResponseDTO.class);
	}

	@Override
	public List<MenuResponseDTO> getAllMenuItems() {

		List<Menu> allMenus = menurepo.findAll();

		// ✅ NEW: check if menu list is empty
		if (allMenus.isEmpty()) {
			throw new EmptyMenuListException("No menu items found");
		}

		return allMenus.stream().map(menu -> modelMapper.map(menu, MenuResponseDTO.class)).toList();
	}

	@Override
	public MenuResponseDTO getMenuItemById(Long menuId) {

		Menu menuEntity = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("There is no menu with ID: " + menuId));

		return modelMapper.map(menuEntity, MenuResponseDTO.class);
	}

	@Override
	public List<MenuResponseDTO> getMenuItemsByCategory(Long categoryId) {

		categoryrepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("No Category found with ID: " + categoryId));

		List<Menu> menus = menurepo.findByCategoryCategoryId(categoryId);

		// NEW: check if no menus exist under this category
		if (menus.isEmpty()) {
			throw new EmptyMenuListException("No menu items found for category ID: " + categoryId);
		}

		return menus.stream().map(menu -> modelMapper.map(menu, MenuResponseDTO.class)).toList();
	}

	@Override
	public MenuResponseDTO updateMenuItem(Long menuId, MenuRequestDTO dto) {

		// 1. Fetch existing menu
		Menu existingMenu = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("Menu item not found with ID: " + menuId));

		// 2. Update basic fields
		existingMenu.setItemName(dto.getItemName());
		existingMenu.setDescription(dto.getDescription());
		existingMenu.setPrice(dto.getPrice());
		existingMenu.setIsVeg(dto.getIsVeg());
		existingMenu.setMenuStatus(dto.getMenuStatus() != null ? dto.getMenuStatus() : existingMenu.getMenuStatus());

		// 3. Category update
		Category category = categoryrepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + dto.getCategoryId()));
		existingMenu.setCategory(category);

		// 4. Image handling
		if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
			// Delete old image from S3
			if (existingMenu.getImageUrl() != null && !existingMenu.getImageUrl().isBlank()) {
				s3Service.deleteFile(existingMenu.getImageUrl());
			}
			// Upload new image
			String newImageUrl = s3Service.uploadFile(dto.getImageFile().getOriginalFilename(), dto.getImageFile());
			existingMenu.setImageUrl(newImageUrl);

		} else if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
			// Use provided URL if no file uploaded
			existingMenu.setImageUrl(dto.getImageUrl());
		}
		// else: keep existing image as-is

		// 5. ✅ Recalculate discount using DTO values (not old DB values!)
		if (dto.getDiscountPercent() != null && dto.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {

			// Validate range
			if (dto.getDiscountPercent().compareTo(BigDecimal.valueOf(100)) > 0) {
				throw new IllegalArgumentException("Discount percent cannot exceed 100%");
			}

			BigDecimal discountAmount = dto.getPrice().multiply(dto.getDiscountPercent())
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

			BigDecimal discountedPrice = dto.getPrice().subtract(discountAmount);

			existingMenu.setDiscountPercent(dto.getDiscountPercent()); // ✅ from DTO
			existingMenu.setDiscountedPrice(discountedPrice); // ✅ recalculated

		} else {
			// No discount — full price
			existingMenu.setDiscountPercent(BigDecimal.ZERO);
			existingMenu.setDiscountedPrice(dto.getPrice());
		}

		// 6. Inventory update
		if (dto.getInventoryIds() != null && !dto.getInventoryIds().isEmpty()) {
			List<Inventory> inventoryItems = inventoryrepo.findAllById(dto.getInventoryIds());
			if (inventoryItems.size() != dto.getInventoryIds().size()) {
				throw new InventoryNotFoundException("One or more Inventory IDs not found");
			}
			existingMenu.setInventoryItems(inventoryItems);
		} else {
			// Clear inventory if none provided
			existingMenu.setInventoryItems(null);
		}

		// 7. Save and return
		Menu updatedMenu = menurepo.save(existingMenu);
		return modelMapper.map(updatedMenu, MenuResponseDTO.class);
	}

	public void deleteMenuItem(Long menuId) {

		Menu menu = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("Menu item not found with ID: " + menuId));

		if (menu.getImageUrl() != null && !menu.getImageUrl().isBlank()) {
			s3Service.deleteFile(menu.getImageUrl());
		}

		menurepo.delete(menu);
	}

	@Override
	public MenuResponseDTO updateMenuStatus(Long menuId, MenuStatus status) {

		Menu menuEntity = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("Menu with ID not found: " + menuId));

		menuEntity.setMenuStatus(status);
		menurepo.save(menuEntity);
		return modelMapper.map(menuEntity, MenuResponseDTO.class);
	}

	@Override
	public List<MenuResponseDTO> getAvailableMenuItems(MenuStatus menuStatus) {

		List<Menu> menuEntity = menurepo.findByMenuStatus(menuStatus);

		// ✅ NEW: check if no menus found for this status
		if (menuEntity.isEmpty()) {
			throw new EmptyMenuListException("No menu items found with status: " + menuStatus);
		}

		return menuEntity.stream().map(menu -> modelMapper.map(menu, MenuResponseDTO.class)) // ✅ fixed bug: menu not
																								// menuEntity
				.toList();
	}

	@Override
	public MenuResponseDTO applyDiscount(Long menuId, BigDecimal discountPercent) {

		Menu menu = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("Menu item not found with ID: " + menuId));

		if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
			throw new DiscountNotApplicable("Discount percent must be between 0 and 100");
		}

		BigDecimal discountAmount = menu.getPrice().multiply(discountPercent).divide(BigDecimal.valueOf(100), 2,
				RoundingMode.HALF_UP);

		menu.setDiscountPercent(discountPercent);
		menu.setDiscountedPrice(menu.getPrice().subtract(discountAmount));

		Menu updatedMenu = menurepo.save(menu);
		return modelMapper.map(updatedMenu, MenuResponseDTO.class);
	}

}
