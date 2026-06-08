package com.superBoy.FoodieHub.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.Enums.CategoryStatus;
import com.superBoy.FoodieHub.I_Service.ICategoryRequestService;
import com.superBoy.FoodieHub.Response.DTOs.CategoryResponseDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.superBoy.FoodieHub.Request.DTOs.CategoryRequestDTO;
import com.superBoy.FoodieHub.Update.CategoryUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Food categories with optional image upload")
public class CategoryController {

	private final ICategoryRequestService categoryService;

	@Autowired
	public CategoryController(ICategoryRequestService categoryService) {
		this.categoryService = categoryService;
	}

	@Operation(summary = "Create a category", description = "Creates a new food category. Optionally accepts an image file via multipart/form-data.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Category created"),
		@ApiResponse(responseCode = "400", description = "Validation error")
	})
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<CategoryResponseDTO> createCategory(@ModelAttribute @Valid CategoryRequestDTO dto) {
		CategoryResponseDTO response = categoryService.addCategory(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Get category by ID", description = "Fetches a single food category by its unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category found"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	@Operation(summary = "Get all categories", description = "Returns all food categories sorted in ascending order by name.")
	@ApiResponse(responseCode = "200", description = "List of categories")
	@GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategoryAscdingOrder());
	}

	@Operation(summary = "Update category", description = "Fully replaces a category's name and/or image.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category updated"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<CategoryUpdateDTO> updateCategory(@PathVariable Long id,
			@ModelAttribute @Valid CategoryUpdateDTO dto) {
		return ResponseEntity.ok(categoryService.updateCategoryById(id, dto));
	}

	@Operation(summary = "Upload / replace category image", description = "Dedicated endpoint to upload or replace the image for an existing category.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Image uploaded"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@PutMapping("/{id}/image")
	public ResponseEntity<CategoryResponseDTO> uploadCategoryImage(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(categoryService.uploadCategoryImage(id, file));
	}

	@Operation(summary = "Update category status", description = "Toggles the status of a category between ACTIVE and INACTIVE.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Status updated"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@PatchMapping("/{id}/status")
	public ResponseEntity<CategoryResponseDTO> updateCategoryStatus(@PathVariable Long id,
			@RequestParam CategoryStatus status) {
		return ResponseEntity.ok(categoryService.updateCategoryStatus(id, status));
	}

	@Operation(summary = "Delete category", description = "Permanently removes a food category by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Category deleted"),
		@ApiResponse(responseCode = "404", description = "Category not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategoryById(id);
		return ResponseEntity.ok("Category deleted successfully with ID" + id);
	}
}
