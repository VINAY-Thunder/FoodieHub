package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.Enums.MenuStatus;
import com.superBoy.FoodieHub.Enums.OrderStatus;
import com.superBoy.FoodieHub.Enums.OrderType;
import com.superBoy.FoodieHub.ExceptionHandling.AddressNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.InvalidOrderException;
import com.superBoy.FoodieHub.ExceptionHandling.MenuNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.OrderNotFoundException;
import com.superBoy.FoodieHub.I_Service.IOrderService;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Model.Menu;
import com.superBoy.FoodieHub.Model.OrderItem;
import com.superBoy.FoodieHub.Model.Orders;
import com.superBoy.FoodieHub.Repository.CustomerAddressRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Repository.MenuRepository;
import com.superBoy.FoodieHub.Repository.OrderItemRepository;
import com.superBoy.FoodieHub.Repository.OrderRepository;
import com.superBoy.FoodieHub.Request.DTOs.OrderItemRequestDTO;
import com.superBoy.FoodieHub.Request.DTOs.OrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderResponseDTO;

@Service
public class OrderService implements IOrderService {

	private CustomerAddressRepository addressrepo;
	private CustomerRepository customerrepo;
	private OrderRepository orderrepo;
	private OrderItemRepository orderitemrepo;
	private MenuRepository menurepo;
	private ModelMapper modelMapper;

	@Autowired
	public OrderService(CustomerAddressRepository addressrepo, CustomerRepository customerrepo,
			OrderRepository orderrepo, OrderItemRepository orderitemrepo, MenuRepository menurepo,
			ModelMapper modelMapper) {
		super();
		this.addressrepo = addressrepo;
		this.customerrepo = customerrepo;
		this.orderrepo = orderrepo;
//		this.orderitemrepo = orderitemrepo;
		this.menurepo = menurepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws CustomerNotFoundException {
		// step1: validate Customer
		Customer customer = customerrepo.findById(orderRequestDTO.getCustomerId()).orElseThrow(
				() -> new CustomerNotFoundException("customer Not found with Id" + orderRequestDTO.getCustomerId()));

		// step2: validate Address(Only for delivery)
		Long addressId = orderRequestDTO.getCustomerAddressId();
		CustomerAddress address = null;
		if (orderRequestDTO.getOrderType() == OrderType.DELIVERY) {
			address = addressrepo.findById(addressId)
					.orElseThrow(() -> new AddressNotFoundException("Address not found with ID: " + addressId));
			if (!address.getCustomer().getCustomerId().equals(orderRequestDTO.getCustomerId())) {
				throw new AddressNotFoundException("This address does not belong to this customer");
			}
		}

		// Validate Table Number — ONLY if DINE_IN
		if (orderRequestDTO.getOrderType() == OrderType.DINE_IN) {
			if (orderRequestDTO.getTableNumber() == null || orderRequestDTO.getTableNumber().isBlank()) {
				throw new InvalidOrderException("Table number is required for DINE_IN orders");
			}
		}

		// step3: validate & Build OrderItems
		List<OrderItem> orderItemsList = new ArrayList<OrderItem>();
		BigDecimal totalAmount = BigDecimal.ZERO;

		Menu menuEntity = null;
		for (OrderItemRequestDTO orderitem : orderRequestDTO.getOrderItems()) {
			// Validate menu exists
			menuEntity = menurepo.findById(orderitem.getMenuId())
					.orElseThrow(() -> new MenuNotFoundException("Menu Doesnot Exits with Id" + orderitem.getMenuId()));

			// Check menu available
			if (menuEntity.getMenuStatus() == MenuStatus.UNAVAILABLE) {
				throw new MenuNotFoundException("Requesting Menu Is not Avaiable: " + menuEntity.getItemName());
			}

			// Get snapshot price
			BigDecimal unitPrice = (menuEntity.getDiscountedPrice() != null
					&& menuEntity.getDiscountedPrice().compareTo(BigDecimal.ZERO) > 0) ? menuEntity.getDiscountedPrice()
							: menuEntity.getPrice();

			// Calculate subtotal
			BigDecimal subtotal = unitPrice.multiply(orderitem.getQuantity());
			totalAmount = totalAmount.add(subtotal);

			OrderItem orderItem = new OrderItem();
			orderItem.setMenu(menuEntity);
			orderItem.setQuantity(orderitem.getQuantity());
			orderItem.setSubtotal(subtotal);
			orderItem.setUnitPrice(unitPrice);
			orderItemsList.add(orderItem);
		}

		// STEP 5: Apply coupon discount
		BigDecimal discountAmount = BigDecimal.ZERO;
		if (orderRequestDTO.getCouponCode() != null && !orderRequestDTO.getCouponCode().isBlank()) {

			// example: "SAVE10" = 10% off
			if (orderRequestDTO.getCouponCode().equals("SAVE10")) {
				discountAmount = totalAmount.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
			}
		}

		// STEP 6: Tax & Delivery
		// 5% GST
		BigDecimal taxAmount = totalAmount.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);

		BigDecimal deliveyCharge = (orderRequestDTO.getOrderType() == OrderType.DELIVERY) 
				? BigDecimal.valueOf(40) // 40																						// delivery																								// charge
				: BigDecimal.ZERO;

		BigDecimal finalAmount = totalAmount.subtract(discountAmount).add(deliveyCharge).add(taxAmount);

		// STEP 7 & 8: Build Order
		Orders order = new Orders();
		order.setCustomer(customer);
		order.setCustomerAddress(address);
		order.setDeliveryCharge(deliveyCharge);
		order.setDiscountAmount(discountAmount);
//		order.setFinalAmount(finalAmount);
		order.setTotalAmount(totalAmount);   
		order.setOrderItems(orderItemsList);
		order.setTaxAmount(taxAmount);
		order.setOrderType(orderRequestDTO.getOrderType());
		order.setCouponCode(orderRequestDTO.getCouponCode());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setSpecialInstructions(orderRequestDTO.getSpecialInstructions());
		order.setTableNumber(orderRequestDTO.getTableNumber());

//		Link each OrderItem → Order
		for (OrderItem item : orderItemsList) {
			item.setOrder(order); // link order to each item
		}
		order.setOrderItems(orderItemsList);

		Orders orderSaved = orderrepo.save(order);

		return modelMapper.map(orderSaved, OrderResponseDTO.class);

	}

	@Override
	public OrderResponseDTO getOrderById(Long orderId) {
		Orders orderEnity = orderrepo.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
		return modelMapper.map(orderEnity, OrderResponseDTO.class);
	}

	@Override
	public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) throws CustomerNotFoundException {
		customerrepo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("This Customer hasn't ordered yet, customerId : " + customerId));

		List<Orders> orderEntity = orderrepo.findByCustomerCustomerId(customerId);
		return orderEntity.stream().map(order -> modelMapper.map(order, OrderResponseDTO.class)).toList();
	}

	@Override
	public List<OrderResponseDTO> getAllOrders() {
		List<Orders> orderList = orderrepo.findAll();

		List<OrderResponseDTO> orderResponseList = orderList.stream()
				.map(order -> modelMapper.map(order, OrderResponseDTO.class)).toList();
		return orderResponseList;
	}

	@Override // UPDATE ORDER STATUS (Admin)
	public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {

		Orders order = orderrepo.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		// Cannot update status of already CANCELLED order
		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderException("Cannot update status of a CANCELLED order");
		}

		// Cannot update status of already DELIVERED order
		if (order.getOrderStatus() == OrderStatus.DELIVERED) {
			throw new InvalidOrderException("Cannot update status of a DELIVERED order");
		}	

		order.setOrderStatus(newStatus);
		Orders updatedOrder = orderrepo.save(order);
		return modelMapper.map(updatedOrder, OrderResponseDTO.class);
	}

	@Override
	public OrderResponseDTO cancelOrder(Long orderId, String cancellationReason) {
		// STEP 1: Find order
		Orders order = orderrepo.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

		// STEP 2: Check if cancellable
		if (order.getOrderStatus() != OrderStatus.PENDING && order.getOrderStatus() != OrderStatus.CONFIRMED) {
			throw new InvalidOrderException("Order cannot be cancelled. Current status: " + order.getOrderStatus());
		}

		order.setOrderStatus(OrderStatus.CANCELLED);
		order.setCancellationReason(cancellationReason);

		orderrepo.save(order);
		return modelMapper.map(order, OrderResponseDTO.class);
	}

	@Override
	public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {

		List<Orders> orderEntityList = orderrepo.findByOrderStatus(status);
		if (orderEntityList.isEmpty() == true) {
			throw new OrderNotFoundException("Order Not Found with OrderStatus" + status);
		}
		return orderEntityList.stream().map(orderStatus -> modelMapper.map(orderStatus, OrderResponseDTO.class))
				.toList();
	}

	@Override
	public List<OrderResponseDTO> getOrdersByOrderType(OrderType orderType) {
		List<Orders> orderEntityList = orderrepo.findByOrderType(orderType);

		if (orderEntityList.isEmpty() == true) {
			throw new OrderNotFoundException("Order Not Found with Odertype: " + orderType);
		}

		return orderEntityList.stream().map(orderStatus -> modelMapper.map(orderStatus, OrderResponseDTO.class))
				.toList();
	}

	@Override
	public void deleteOrder(Long orderId) {
		orderrepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not Found with Id " + orderId));
		orderrepo.deleteById(orderId);
	}
	
}
