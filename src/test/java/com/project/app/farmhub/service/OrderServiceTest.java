package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusPayment;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.repository.OrderRepository;
import com.project.app.farmhub.request.CancelOrderRequest;
import com.project.app.farmhub.request.CreateOrderRequest;
import com.project.app.farmhub.response.OrderResponse;
import com.project.app.farmhub.service.impl.OrderServiceImpl;
import com.project.app.farmhub.service.impl.UserDetailsServiceImp;

class OrderServiceTest {

	@Mock
	private OrderRepository repository;

	@Mock
	private UserDetailsServiceImp userService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private OrderServiceImpl orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddOrderWithInvalidProduct() {
		CreateOrderRequest request = new CreateOrderRequest();
		request.setProductId("1");
		request.setQuantity(5);

		when(productService.getEntityById("1")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.add(request);
		});

		assertEquals("product " + ErrorMessageConstant.IS_NOT_EXISTS, exception.getMessage());
	}

	@Test
	void testCancelOrder() {
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId("1");

		Order mockOrder = new Order();
		mockOrder.setId("1");
		mockOrder.setStatus(StatusOrder.PENDING);
		mockOrder.setQuantity(5);

		Product mockProduct = new Product();
		mockProduct.setId("1");
		mockProduct.setStock(10);

		mockOrder.setProduct(mockProduct);

		when(repository.findById("1", Order.class)).thenReturn(Optional.of(mockOrder));
		when(productService.getEntityById("1")).thenReturn(Optional.of(mockProduct));
		when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		orderService.cancelOrder(request);

		assertEquals(StatusOrder.CANCELLED, mockOrder.getStatus());
		verify(productService, times(1)).changeTotalStock(mockProduct);
		verify(repository, times(1)).save(mockOrder);
	}

	@Test
	void testCancelOrderWithInvalidStatus() {
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId("1");

		Order mockOrder = new Order();
		mockOrder.setId("1");
		mockOrder.setStatus(StatusOrder.SHIPPED);

		when(repository.findById("1", Order.class)).thenReturn(Optional.of(mockOrder));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.cancelOrder(request);
		});

		assertEquals("Only orders with status PENDING can be canceled", exception.getMessage());
	}

	@Test
	void testCancelOrderWithInvalidOrder() {
		CancelOrderRequest request = new CancelOrderRequest();
		request.setId("1");

		when(repository.findById("1", Order.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.cancelOrder(request);
		});

		assertEquals("Order not found with id: 1", exception.getMessage());
	}

	@Test
	void testDeleteOrder() {
		String orderId = "1";
		Order existingOrder = new Order();
		existingOrder.setId("1");

		when(repository.findById(orderId, Order.class)).thenReturn(Optional.of(existingOrder));

		orderService.delete(orderId);

		verify(repository, times(1)).delete(existingOrder);
	}

	@Test
	void testDeleteNonExistentOrder() {
		String orderId = "1";

		when(repository.findById(orderId, Order.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			orderService.delete(orderId);
		});

		assertEquals("id is not exist", exception.getMessage());
	}

	@Test
	void testGetById() {
		String orderId = "1";

		Order mockOrder = new Order();

		User farmer = new User();
		farmer.setId("1");
		farmer.setUsername("Agus");

		User umkm = new User();
		umkm.setId("2");
		umkm.setUsername("Rian");

		Product product = new Product();
		product.setId("1");
		product.setCode("TIMUN");
		product.setName("Timun");
		product.setFarmer(farmer);

		mockOrder.setId("orderId");
		mockOrder.setStatus(StatusOrder.PENDING);
		mockOrder.setStatusPayment(StatusPayment.PENDING);
		mockOrder.setProduct(product);
		mockOrder.setFarmer(farmer);
		mockOrder.setUmkm(umkm);

		mockOrder.setId("1");
		mockOrder.setStatus(StatusOrder.PENDING);

		when(repository.findById(orderId, Order.class)).thenReturn(Optional.of(mockOrder));

		OrderResponse response = orderService.getById(orderId);

		assertEquals(mockOrder.getId(), response.getId());
	}

	@Test
	void testGetAll() {
		Order mockOrder1 = new Order();
		mockOrder1.setId("1");
		mockOrder1.setStatus(StatusOrder.PENDING);

		Order mockOrder2 = new Order();
		mockOrder2.setId("2");
		mockOrder2.setStatus(StatusOrder.SHIPPED);

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

		mockOrder1.setProduct(product);
		mockOrder2.setProduct(product);
		mockOrder1.setFarmer(farmer);
		mockOrder2.setFarmer(farmer);
		mockOrder1.setUmkm(umkm);
		mockOrder2.setUmkm(umkm);
		mockOrder1.setStatus(StatusOrder.PENDING);
		mockOrder1.setStatusPayment(StatusPayment.PENDING);
		mockOrder2.setStatus(StatusOrder.PENDING);
		mockOrder2.setStatusPayment(StatusPayment.PENDING);

		when(repository.findAll(Order.class)).thenReturn(List.of(mockOrder1, mockOrder2));

		List<OrderResponse> responses = orderService.getAll();

		assertEquals(2, responses.size());
	}

	@Test
	void testChangeStatusPayment() {
		Order order = new Order();
		order.setId("1");
		order.setStatusPayment(StatusPayment.PENDING);

		when(repository.save(order)).thenReturn(order);

		Order updatedOrder = orderService.changeStatusPayment(order);

		assertEquals(StatusPayment.PENDING, updatedOrder.getStatusPayment());
	}

	@Test
	void testChangeStatus() {
		Order order = new Order();
		order.setId("1");
		order.setStatus(StatusOrder.PENDING);

		when(repository.save(order)).thenReturn(order);

		Order updatedOrder = orderService.changeStatus(order);

		assertEquals(StatusOrder.PENDING, updatedOrder.getStatus());
	}

}