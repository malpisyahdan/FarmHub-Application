package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusPayment;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.OrderRepository;
import com.project.app.farmhub.request.CancelOrderRequest;
import com.project.app.farmhub.request.CreateOrderRequest;
import com.project.app.farmhub.response.OrderResponse;
import com.project.app.farmhub.response.ProductResponse;
import com.project.app.farmhub.service.impl.OrderServiceImpl;

class OrderServiceTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private UserDetailsServiceImp userService;

	@Mock
	private ProductService productService;

	private MockedStatic<SecurityHelper> securityHelperMock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		securityHelperMock = mockStatic(SecurityHelper.class);
		Mockito.lenient().when(orderRepository.findById(any())).thenReturn(Optional.empty());
	}

	@AfterEach
	void tearDown() {
		securityHelperMock.close();
	}

	@Test
	void testAddOrder() {
		User farmer = new User();
		farmer.setId("1");
		farmer.setUsername("Agus");

		User umkm = new User();
		umkm.setId("2");
		umkm.setUsername("Rian");

		Product product = new Product();
		product.setId("2");
		product.setStock(10);
		product.setPrice(BigDecimal.valueOf(100));
		product.setFarmer(farmer);

		ProductResponse productResponse = new ProductResponse();
		productResponse.setFarmerId(farmer.getId());
		productResponse.setPrice(BigDecimal.valueOf(100));

		CreateOrderRequest request = new CreateOrderRequest();
		request.setProductId(product.getId());
		request.setQuantity(5);

		when(productService.getEntityById("2")).thenReturn(Optional.of(product));
		when(productService.getById("2")).thenReturn(productResponse);
		when(SecurityHelper.getCurrentUserId()).thenReturn("1");
		when(userService.getEntityById("1")).thenReturn(Optional.of(farmer));
		when(userService.getEntityById("2")).thenReturn(Optional.of(umkm));

		orderService.add(request);

		verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
	}

	@Test
	void testAddOrderInvalidProduct() {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setProductId("productId");
		request.setQuantity(5);

		when(productService.getEntityById(anyString())).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.add(request));
		assertEquals("product " + ErrorMessageConstant.IS_NOT_EXISTS, exception.getReason());
	}

	@Test
	void testCancelOrder() {
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId("orderId");

		Order order = new Order();
		order.setStatus(StatusOrder.PENDING);
		Product product = new Product();
		product.setId("1");
		product.setStock(10);
		order.setProduct(product);
		order.setQuantity(5);

		when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));
		when(productService.getEntityById(anyString())).thenReturn(Optional.of(product));

		orderService.cancelOrder(request);

		assertEquals(StatusOrder.CANCELLED, order.getStatus());
		assertEquals(15, product.getStock());
		verify(orderRepository, times(1)).save(order);
		verify(productService, times(1)).changeTotalStock(product);
	}

	@Test
	void testCancelOrderInvalidStatus() {
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId("orderId");

		Order order = new Order();
		order.setStatus(StatusOrder.SHIPPED);

		when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> orderService.cancelOrder(request));
		assertEquals("Only orders with status PENDING can be canceled", exception.getMessage());
	}

	@Test
	void testGetById() {
		String productId = "1";

		User farmer = new User();
		farmer.setId("1");
		farmer.setUsername("Agus");

		User umkm = new User();
		umkm.setId("2");
		umkm.setUsername("Rian");

		Product product = new Product();
		product.setId(productId);
		product.setCode("TIMUN");
		product.setName("Timun");
		product.setFarmer(farmer);

		Order order = new Order();
		order.setId("orderId");
		order.setStatus(StatusOrder.PENDING);
		order.setStatusPayment(StatusPayment.PENDING);
		order.setProduct(product);
		order.setFarmer(farmer);
		order.setUmkm(umkm);

		when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

		OrderResponse response = orderService.getById("orderId");

		assertNotNull(response);
		assertEquals("orderId", response.getId());
		assertEquals(StatusOrder.PENDING.getStatusString(), response.getStatus());
		assertEquals(StatusPayment.PENDING.getStatusString(), response.getStatusPayment());
	}

	@Test
	void testGetByIdNotFound() {
		when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.getById("orderId"));
		assertEquals("id is not exist", exception.getReason());
	}

	@Test
	void testDelete() {
		Order order = new Order();
		order.setId("orderId");

		when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

		orderService.delete("orderId");

		verify(orderRepository, times(1)).deleteById("orderId");
	}

	@Test
	void testDeleteNotFound() {
		when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> orderService.delete("orderId"));
		assertEquals("id is not exist", exception.getReason());
	}

	@Test
	void testChangeStatus() {
		Order order = new Order();
		order.setId("orderId");
		order.setStatus(StatusOrder.PENDING);

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		Order updatedOrder = orderService.changeStatus(order);

		assertNotNull(updatedOrder);
		assertEquals(order.getId(), updatedOrder.getId());
	}

	@Test
	void testChangeStatusPayment() {
		Order order = new Order();
		order.setId("orderId");
		order.setStatusPayment(StatusPayment.PENDING);

		when(orderRepository.save(any(Order.class))).thenReturn(order);

		Order updatedOrder = orderService.changeStatusPayment(order);

		assertNotNull(updatedOrder);
		assertEquals(order.getId(), updatedOrder.getId());
	}

	@Test
	void testGetAll() {

		String productId = "1";

		User farmer = new User();
		farmer.setId("1");
		farmer.setUsername("Agus");

		User umkm = new User();
		umkm.setId("2");
		umkm.setUsername("Rian");

		Product product = new Product();
		product.setId(productId);
		product.setCode("TIMUN");
		product.setName("Timun");
		product.setFarmer(farmer);

		Order order = new Order();
		order.setId("orderId");
		order.setStatus(StatusOrder.PENDING);
		order.setStatusPayment(StatusPayment.PENDING);
		order.setProduct(product);
		order.setFarmer(farmer);
		order.setUmkm(umkm);

		when(orderRepository.findAll()).thenReturn(List.of(order));

		List<OrderResponse> responseList = orderService.getAll();

		assertNotNull(responseList);
		assertEquals(1, responseList.size());
		assertEquals("orderId", responseList.get(0).getId());
	}
}
