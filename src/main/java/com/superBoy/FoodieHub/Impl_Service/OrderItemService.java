package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.ExceptionHandling.MenuNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.OrderItemNotFoundException;
import com.superBoy.FoodieHub.I_Service.IOrderItem;
import com.superBoy.FoodieHub.Model.Menu;
import com.superBoy.FoodieHub.Model.OrderItem;
import com.superBoy.FoodieHub.Repository.MenuRepository;
import com.superBoy.FoodieHub.Repository.OrderItemRepository;
import com.superBoy.FoodieHub.Request.DTOs.OrderItemRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderItemResponseDTO;

@Service
public class OrderItemService implements IOrderItem {

	private MenuRepository menurepo;
	private OrderItemRepository orderitemrepo;
	private ModelMapper modelmapper;

	@Autowired
	public OrderItemService(MenuRepository menurepo, OrderItemRepository orderitemrepo, ModelMapper modelmapper) {
		super();
		this.menurepo = menurepo;
		this.orderitemrepo = orderitemrepo;
		this.modelmapper = modelmapper;
	}

	@Override
	public OrderItemResponseDTO createOrderItem(OrderItemRequestDTO orderitemRequestDTO) {
		Long menuId = orderitemRequestDTO.getMenuId();
		Menu menuEntity = menurepo.findById(menuId)
				.orElseThrow(() -> new MenuNotFoundException("Menu not found with Id" + menuId));

		// Check menu is available
		if (menuEntity.getMenuStatus() == MenuStatus.UNAVAILABLE) {
			throw new MenuNotFoundException("Menu is not available with Id" + menuId);
		}

		BigDecimal unitPrice = (menuEntity.getDiscountedPrice() != null
				&& menuEntity.getDiscountedPrice().compareTo(BigDecimal.ZERO) > 0) ? menuEntity.getDiscountedPrice()
						: menuEntity.getPrice();

		BigDecimal quantity = orderitemRequestDTO.getQuantity();
		BigDecimal subtotal = unitPrice.multiply(quantity);

		OrderItem orderitem = new OrderItem();
		orderitem.setMenu(menuEntity);
		// Note: Order must be set before saving, or this remains an orphan item.
		// Usually handle this via OrderService.
		orderitem.setQuantity(quantity);
		orderitem.setSubtotal(subtotal);
		orderitem.setUnitPrice(unitPrice);

		OrderItem savedItem = orderitemrepo.save(orderitem);
		return modelmapper.map(savedItem, OrderItemResponseDTO.class);
	}

	@Override
	public List<OrderItemResponseDTO> getOrderItemsByOrderId(Long orderId) {
		List<OrderItem> items = orderitemrepo.findByOrderOrderId(orderId);
		return items.stream()
				.map(item -> modelmapper.map(item, OrderItemResponseDTO.class))
				.toList();
	}

	@Override
	public OrderItemResponseDTO updateOrderItemQuantity(Long orderItemId, BigDecimal newQuantity) {
		OrderItem item = orderitemrepo.findById(orderItemId)
				.orElseThrow(() -> new OrderItemNotFoundException("Order item not found with ID: " + orderItemId));

		item.setQuantity(newQuantity);
		item.setSubtotal(item.getUnitPrice().multiply(newQuantity));
		
		OrderItem updatedItem = orderitemrepo.save(item);
		return modelmapper.map(updatedItem, OrderItemResponseDTO.class);
	}

	@Override
	public void removeOrderItem(Long orderItemId) {
		if (!orderitemrepo.existsById(orderItemId)) {
			throw new OrderItemNotFoundException("Order item not found with ID: " + orderItemId);
		}
		orderitemrepo.deleteById(orderItemId);
	}

}
