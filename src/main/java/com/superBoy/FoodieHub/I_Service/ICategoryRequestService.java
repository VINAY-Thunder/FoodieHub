package com.superBoy.FoodieHub.I_Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.superBoy.FoodieHub.Request.DTOs.CategoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CategoryResponseDTO;
import com.superBoy.FoodieHub.Update.CategoryUpdateDTO;

public interface ICategoryRequestService {

	CategoryResponseDTO addCategory(CategoryRequestDTO category);

	CategoryResponseDTO getCategoryById(Long categoryId);

	List<CategoryResponseDTO> getAllCategoryAscdingOrder();

	CategoryUpdateDTO updateCategoryById(Long CategoryID,CategoryUpdateDTO categoryUpdateDTO);

	CategoryResponseDTO uploadCategoryImage(Long catgeoryId, MultipartFile file);
	
	void deleteCategoryById(Long categoryId);
	

}
