package com.superBoy.FoodieHub.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Model.Menu;
import com.superBoy.FoodieHub.Model.OrderItem;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.MenuResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderItemResponseDTO;

@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true).setSkipNullEnabled(true).setAmbiguityIgnored(true); // ✅
																															// ADD
																															// THIS
																															// LINE

		// CustomerAddress -> CustomerAddressResponseDTO mapping
		modelMapper.typeMap(CustomerAddress.class, CustomerAddressResponseDTO.class).addMappings(mapper -> mapper
				.map(src -> src.getCustomer().getCustomerId(), CustomerAddressResponseDTO::setCustomerId));

		// Menu -> MenuResponseDTO mapping
		modelMapper.typeMap(Menu.class, MenuResponseDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getCategory().getCategoryId(), MenuResponseDTO::setCategoryId);
			mapper.map(src -> src.getCategory().getCategoryName(), MenuResponseDTO::setCategoryName);
		});

		// OrderItem -> OrderItemResponseDTO mapping
		modelMapper.typeMap(OrderItem.class, OrderItemResponseDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getMenu().getItemName(), OrderItemResponseDTO::setItemName);
			mapper.map(src -> src.getMenu().getMenuId(), OrderItemResponseDTO::setMenuId);
			mapper.map(src -> src.getOrder().getOrderId(), OrderItemResponseDTO::setOrderId);
		});

		return modelMapper;
	}
}