package com.superBoy.FoodieHub.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Model.Menu;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.MenuResponseDTO;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration()
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true);

		// CustomerAddress -> CustomerAddressResponseDTO mapping
		modelMapper.typeMap(CustomerAddress.class, CustomerAddressResponseDTO.class)
				.addMappings(mapper ->
						mapper.map(src -> src.getCustomer().getCustomerId(),
								CustomerAddressResponseDTO::setCustomerId));

		// Menu -> MenuResponseDTO mapping
		modelMapper.typeMap(Menu.class, MenuResponseDTO.class)
				.addMappings(mapper -> {
					mapper.map(src -> src.getCategory().getCategoryId(), MenuResponseDTO::setCategoryId);
					mapper.map(src -> src.getCategory().getCategoryName(), MenuResponseDTO::setCategoryName);
				});

		return modelMapper;
	}
}