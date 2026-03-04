package com.superBoy.FoodieHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByOrderByDisplayOrderAsc();
	
}
