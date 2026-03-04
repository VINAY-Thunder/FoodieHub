package com.superBoy.FoodieHub.Controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import com.superBoy.FoodieHub.I_Service.ICategoryRequestService;
import com.superBoy.FoodieHub.Response.DTOs.CategoryResponseDTO;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.superBoy.FoodieHub.Request.DTOs.CategoryRequestDTO;

import com.superBoy.FoodieHub.Update.CategoryUpdateDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final ICategoryRequestService categoryService;

	@Autowired
	public CategoryController(ICategoryRequestService categoryService) {
		this.categoryService = categoryService;
	}

	// CREATE CATEGORY (WITH OPTIONAL IMAGE)
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<CategoryResponseDTO> createCategory(@ModelAttribute @Valid CategoryRequestDTO dto) {
		CategoryResponseDTO response = categoryService.addCategory(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// GET CATEGORY BY ID
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {

		CategoryResponseDTO response = categoryService.getCategoryById(id);

		return ResponseEntity.ok(response);
	}

	// GET ALL CATEGORIES (ASCENDING ORDER)
	@GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {

		List<CategoryResponseDTO> list = categoryService.getAllCategoryAscdingOrder();

		return ResponseEntity.ok(list);
	}

	// UPDATE CATEGORY (TEXT & OPTIONAL IMAGE)
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<CategoryUpdateDTO> updateCategory(@PathVariable Long id,
			@ModelAttribute @Valid CategoryUpdateDTO dto) {

		CategoryUpdateDTO response = categoryService.updateCategoryById(id, dto);

		return ResponseEntity.ok(response);
	}

	// UPLOAD / REPLACE CATEGORY IMAGE (SEPARATE API)
	@PutMapping("/{id}/image")
	public ResponseEntity<CategoryResponseDTO> uploadCategoryImage(@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {

		CategoryResponseDTO response = categoryService.uploadCategoryImage(id, file);

		return ResponseEntity.ok(response);
	}

	// DELETE CATEGORY
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {

		categoryService.deleteCategoryById(id);

		return ResponseEntity.ok("Category deleted successfully with ID"+id);
	}
}
