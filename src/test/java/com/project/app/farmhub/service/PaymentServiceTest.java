package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
	private PaymentRepository paymentRepository;

	@Mock
	private OrderService orderService;

	@Mock
	private LovDataService lovService;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	private Payment payment;
	
	private CreatePaymentRequest createPaymentRequest;

	@BeforeEach
	public void setUp() {
		Order order = new Order();
		order.setId("order123");
		order.setStatusPayment(StatusPayment.PENDING);

		LovData lov = new LovData();
		lov.setId("lov2");
		lov.setCode("CASH");
		lov.setLovType("PAYMENT-METHOD");

		payment = new Payment();
		payment.setId("payment123");
		payment.setOrder(order);
		payment.setLovPaymentMethod(lov);
		payment.setStatusPayment(StatusPayment.PAID);

		createPaymentRequest = new CreatePaymentRequest();
		createPaymentRequest.setOrderId("order123");
		createPaymentRequest.setLovPaymentMethodId("lov2");
	}

	@Test
	void testAddPayment() {
		when(orderService.getEntityById(anyString())).thenReturn(Optional.of(payment.getOrder()));
		when(lovService.getEntityById(anyString())).thenReturn(Optional.of(payment.getLovPaymentMethod()));

		when(paymentRepository.saveAndFlush(any(Payment.class))).thenReturn(payment);

		paymentService.add(createPaymentRequest);

		verify(paymentRepository, times(1)).saveAndFlush(any(Payment.class));
	}

	@Test
	void testAddPaymentOrderNotFound() {
		when(orderService.getEntityById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			paymentService.add(createPaymentRequest);
		});
	}

	@Test
	void testDeletePayment() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.of(payment));

		paymentService.delete("payment123");

		verify(paymentRepository, times(1)).deleteById("payment123");
	}

	@Test
	void testDeletePaymentNotFound() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			paymentService.delete("payment123");
		});
	}

	@Test
	void testGetEntityById() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.of(payment));

		Optional<Payment> result = paymentService.getEntityById("payment123");

		assertTrue(result.isPresent());
		assertEquals(payment.getId(), result.get().getId());
		assertEquals(payment.getOrder().getId(), result.get().getOrder().getId());
	}

	@Test
	void testGetEntityByIdNotFound() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.empty());

		Optional<Payment> result = paymentService.getEntityById("payment123");

		assertTrue(result.isEmpty());
	}

	@Test
	void testGetById() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.of(payment));

		PaymentResponse response = paymentService.getById("payment123");

		assertNotNull(response);
		assertEquals("payment123", response.getId());
		assertEquals("order123", response.getOrderId());
		assertEquals("PAID", response.getStatusPayment());
	}

	@Test
	void testGetByIdNotFound() {
		when(paymentRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			paymentService.getById("payment123");
		});
	}

	@Test
	void testGetAllPayments() {
		List<Payment> payments = new ArrayList<>();
		payments.add(payment);
		when(paymentRepository.findAll()).thenReturn(payments);

		List<PaymentResponse> responses = paymentService.getAll();

		assertNotNull(responses);
		assertEquals(1, responses.size());

		PaymentResponse response = responses.get(0);
		assertEquals("payment123", response.getId());
		assertEquals("order123", response.getOrderId());
		assertEquals("PAID", response.getStatusPayment());
	}

}
