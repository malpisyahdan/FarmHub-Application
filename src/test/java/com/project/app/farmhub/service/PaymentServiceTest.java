package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.app.farmhub.common.type.StatusPayment;
import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Payment;
import com.project.app.farmhub.repository.PaymentRepository;
import com.project.app.farmhub.request.CreatePaymentRequest;
import com.project.app.farmhub.response.PaymentResponse;
import com.project.app.farmhub.service.impl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentRepository repository;

	@Mock
	private OrderService orderService;

	@Mock
	private LovDataService lovService;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	private CreatePaymentRequest request;
	private Payment payment;
	private Order order;
	private LovData lovData;

	@BeforeEach
	void setUp() {
		request = new CreatePaymentRequest();
		request.setOrderId("order1");
		request.setLovPaymentMethodId("lov1");

		order = new Order();
		order.setId("order1");

		lovData = new LovData();
		lovData.setId("lov1");

		payment = new Payment();
		payment.setId("payment1");
		payment.setOrder(order);
		payment.setLovPaymentMethod(lovData);
		payment.setStatusPayment(StatusPayment.PAID);
	}

	@Test
	void testAddPayment() {
		when(orderService.getEntityById("order1")).thenReturn(Optional.of(order));
		when(lovService.getEntityById("lov1")).thenReturn(Optional.of(lovData));
		when(repository.save(any(Payment.class))).thenReturn(payment);

		paymentService.add(request);

		verify(repository).save(any(Payment.class));
		verify(orderService).changeStatusPayment(any(Order.class));
	}

	@Test
	void testAddPaymentOrderNotFound() {
		when(orderService.getEntityById("order1")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			paymentService.add(request);
		});

		assertEquals("order is not exists", exception.getMessage());
	}

	@Test
	void testDeletePayment() {
		when(repository.findById("payment1", Payment.class)).thenReturn(Optional.of(payment));
		doNothing().when(repository).delete(any(Payment.class));

		paymentService.delete("payment1");

		verify(repository).delete(any(Payment.class));
	}

	@Test
	void testDeletePaymentNotFound() {
		when(repository.findById("payment1", Payment.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			paymentService.delete("payment1");
		});

		assertEquals("id is not exist", exception.getMessage());
	}

	@Test
	void testGetById() {
		when(repository.findById("payment1", Payment.class)).thenReturn(Optional.of(payment));

		PaymentResponse response = paymentService.getById("payment1");

		assertNotNull(response);
		assertEquals("payment1", response.getId());
		assertEquals("order1", response.getOrderId());
	}

	@Test
	void testGetByIdNotFound() {
		when(repository.findById("payment1", Payment.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			paymentService.getById("payment1");
		});

		assertEquals("id is not exist", exception.getMessage());
	}

	@Test
	void testGetAll() {
		when(repository.findAll(Payment.class)).thenReturn(List.of(payment));

		List<PaymentResponse> responses = paymentService.getAll();

		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertEquals("payment1", responses.get(0).getId());
	}
}
