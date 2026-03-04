package com.superBoy.FoodieHub.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.Model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findByCategoryCategoryId(Long categoryId);
	
//	getAvailableMenuItems()
	List<Menu> findByMenuStatus(MenuStatus menuStatus);
	

}
