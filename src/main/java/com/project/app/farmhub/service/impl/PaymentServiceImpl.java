package com.project.app.farmhub.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.common.type.StatusPayment;
import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Payment;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.repository.PaymentRepository;
import com.project.app.farmhub.request.CreatePaymentRequest;
import com.project.app.farmhub.response.PaymentResponse;
import com.project.app.farmhub.service.LovDataService;
import com.project.app.farmhub.service.OrderService;
import com.project.app.farmhub.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository repository;
	private final OrderService orderService;
	private final LovDataService lovService;

	private static final String PROP_ORDER = "order ";

	@Transactional
	@Override
	public void add(CreatePaymentRequest request) {
		validateNonBk(request);
		Payment entity = new Payment();
		mapToEntity(entity, request);
		repository.saveAndFlush(entity);

	}

	private void validateNonBk(CreatePaymentRequest request) {
		orderService.getEntityById(request.getOrderId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						PROP_ORDER + ErrorMessageConstant.IS_NOT_EXISTS));

	}

	private void mapToEntity(Payment entity, CreatePaymentRequest request) {

		Order order = orderService.getEntityById(request.getOrderId())
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + request.getOrderId()));

		entity.setOrder(order);
		LovData lov = lovService.getEntityById(request.getLovPaymentMethodId())
				.orElseThrow(() -> new IllegalArgumentException("lov not found with id: " + request.getLovPaymentMethodId()));
		
		entity.setLovPaymentMethod(lov);
		entity.setStatusPayment(StatusPayment.PAID);

		order.setStatusPayment(StatusPayment.PAID);

		orderService.changeStatusPayment(order);

	}

	@Override
	public void delete(String id) {
		repository.findById(id).ifPresentOrElse(entity -> repository.deleteById(id), () -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is not exist");
		});

	}

	@Override
	public Optional<Payment> getEntityById(String id) {
		return repository.findById(id);
	}

	@Override
	public PaymentResponse getById(String id) {
		Payment entity = getEntityById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "id" + " is not exist"));
		return mapToResponse(entity);
	}

	@Override
	public List<PaymentResponse> getAll() {
		List<Payment> detail = repository.findAll();
		return detail.stream().map(this::mapToResponse).toList();
	}

	private PaymentResponse mapToResponse(Payment entity) {
		PaymentResponse response = new PaymentResponse();
		response.setId(entity.getId());
		response.setOrderId(entity.getOrder().getId());
		response.setPaymentMethod(entity.getLovPaymentMethod().getName());
		response.setStatusPayment(entity.getStatusPayment().getStatusString());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		return response;
	}

}
