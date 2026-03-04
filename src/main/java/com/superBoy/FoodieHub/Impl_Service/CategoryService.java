package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.superBoy.FoodieHub.ExceptionHandling.CategoryNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.InvalidFileException;
import com.superBoy.FoodieHub.I_Service.ICategoryRequestService;
import com.superBoy.FoodieHub.Model.Category;
import com.superBoy.FoodieHub.Repository.CategoryRepository;
import com.superBoy.FoodieHub.Request.DTOs.CategoryRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CategoryResponseDTO;
import com.superBoy.FoodieHub.Update.CategoryUpdateDTO;

@Service
public class CategoryService implements ICategoryRequestService {

	private ModelMapper modelMapper;
	private CategoryRepository categoryRepo;
	private S3Service s3Service;

	@Autowired
	public CategoryService(CategoryRepository categoryRepo, S3Service s3Service, ModelMapper modelMapper) {
		this.categoryRepo = categoryRepo;
		this.s3Service = s3Service;
		this.modelMapper = modelMapper;
	}

	@Override // CREATE CATEGORY (WITH OPTIONAL IMAGE)
	public CategoryResponseDTO addCategory(CategoryRequestDTO categoryReq) {

		Category catgoryEntity = modelMapper.map(categoryReq, Category.class);

		// Handle image upload if provided during creation
		MultipartFile file = categoryReq.getImageFile();
		if (file != null && !file.isEmpty()) {
			if (!file.getContentType().startsWith("image/")) {
				throw new InvalidFileException("Only image files are allowed");
			}
			// String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
			// // s3Service already handles UUID
			String imageUrl = s3Service.uploadFile(file.getOriginalFilename(), file);
			catgoryEntity.setImageUrl(imageUrl);
		}

		categoryRepo.save(catgoryEntity);
		return modelMapper.map(catgoryEntity, CategoryResponseDTO.class);
	}

	@Override // GET BY ID
	public CategoryResponseDTO getCategoryById(Long categoryId) {
		Category catgoryEntity = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		return modelMapper.map(catgoryEntity, CategoryResponseDTO.class);
	}

	@Override // GET ALL (ASCENDING ORDER)in ASC Order
	public List<CategoryResponseDTO> getAllCategoryAscdingOrder() {
		List<Category> catgoryEntity = categoryRepo.findAllByOrderByDisplayOrderAsc();

		return catgoryEntity.stream().map(category -> modelMapper.map(category, CategoryResponseDTO.class)).toList();
	}

	@Override // UPDATE CATEGORY (TEXT & OPTIONAL IMAGE)
	public CategoryUpdateDTO updateCategoryById(Long categoryId, CategoryUpdateDTO categoryUpdateDTO) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category is not found by Id: " + categoryId));

		// Handle image update if a new file is provided
		MultipartFile file = categoryUpdateDTO.getImageFile();
		if (file != null && !file.isEmpty()) {
			if (!file.getContentType().startsWith("image/")) {
				throw new InvalidFileException("Only image files are allowed");
			}

			// Delete old image from S3 if it exists
			if (category.getImageUrl() != null && !category.getImageUrl().isBlank()) {
				s3Service.deleteFile(category.getImageUrl());
			}

			// Upload new image
			String imageUrl = s3Service.uploadFile(file.getOriginalFilename(), file);
			category.setImageUrl(imageUrl);
		}

		modelMapper.map(categoryUpdateDTO, category); // maps text fields
		Category updatedCategory = categoryRepo.save(category);

		return modelMapper.map(updatedCategory, CategoryUpdateDTO.class);
	}

	// UPLOAD / REPLACE IMAGE
	@Override
	public CategoryResponseDTO uploadCategoryImage(Long categoryId, MultipartFile file) {

		// 1️ Check if file is null or empty
		if (file == null || file.isEmpty()) {
			throw new InvalidFileException("File is empty");
		}

		// 2️ Allow only image files (image/png, image/jpeg etc)
		if (!file.getContentType().startsWith("image/")) {
			throw new InvalidFileException("Only image files are allowed");
		}

		// 3️ Fetch category from database
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

		// 4️ If category already has an image → delete old image from S3
		if (category.getImageUrl() != null && !category.getImageUrl().isBlank()) {

			s3Service.deleteFile(category.getImageUrl());
		}

		// 5️ Create unique filename (to avoid duplicate names)
		String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// 6️ Upload file to S3 bucket
		String imageUrl = s3Service.uploadFile(uniqueFileName, file);

		// 7️ Save the returned S3 URL inside category entity
		category.setImageUrl(imageUrl);

		// 8️ Save updated category into database
		Category saved = categoryRepo.save(category);

		// 9️ Convert Entity → ResponseDTO and return
		return modelMapper.map(saved, CategoryResponseDTO.class);
	}

	// DELETE
	@Override
	public void deleteCategoryById(Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
		
		if (category.getImageUrl()!= null && !category.getImageUrl().isBlank()) {
			s3Service.deleteFile(category.getImageUrl());
		} else {
			throw new InvalidFileException(
					"File is cannot be Deleted cause it empty" + category.getImageUrl());
		}

		categoryRepo.deleteById(categoryId);

	}
}
