package com.project.app.farmhub.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusShipment;
import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Shipment;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.repository.ShipmentRepository;
import com.project.app.farmhub.request.CreateShipmentRequest;
import com.project.app.farmhub.response.ShipmentResponse;
import com.project.app.farmhub.service.LovDataService;
import com.project.app.farmhub.service.OrderService;
import com.project.app.farmhub.service.ShipmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

	private final ShipmentRepository repository;
	private final OrderService orderService;
	private final LovDataService lovService;

	private static final String PROP_ORDER = "order ";

	@Transactional
	@Override
	public void add(CreateShipmentRequest request) {
		validateNonBk(request);
		Shipment entity = new Shipment();
		mapToEntity(entity, request);
		repository.save(entity);

	}

	private void validateNonBk(CreateShipmentRequest request) {

		orderService.getEntityById(request.getOrderId())
				.orElseThrow(() -> new RuntimeException(PROP_ORDER + ErrorMessageConstant.IS_NOT_EXISTS));

	}

	private void mapToEntity(Shipment entity, CreateShipmentRequest request) {

		Order order = orderService.getEntityById(request.getOrderId())
				.orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

		entity.setOrder(order);
		String trackingNumber = generateTrackingNumber();
		entity.setTrackingNumber(trackingNumber);
		LovData lov = lovService.getEntityById(request.getLovCourierServiceId())
				.orElseThrow(() -> new RuntimeException("lov not found with id: " + request.getLovCourierServiceId()));
		entity.setLovCourierService(lov);
		entity.setStatusShipment(StatusShipment.SHIPPED);

		order.setStatus(StatusOrder.SHIPPED);

		orderService.changeStatusPayment(order);

	}

	@Transactional
	@Override
	public void delete(String id) {
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.delete(entity);
		}, () -> {
			throw new RuntimeException("id is not exist");
		});

	}

	@Override
	public Optional<Shipment> getEntityById(String id) {
		return repository.findById(id, Shipment.class);
	}

	@Override
	public ShipmentResponse getById(String id) {
		Shipment entity = getEntityById(id).orElseThrow(() -> new RuntimeException("id is not exist"));
		return mapToResponse(entity);
	}

	@Override
	public List<ShipmentResponse> getAll() {
		List<Shipment> detail = repository.findAll(Shipment.class);
		return detail.stream().map(this::mapToResponse).toList();
	}

	private ShipmentResponse mapToResponse(Shipment entity) {
		ShipmentResponse response = new ShipmentResponse();
		response.setId(entity.getId());
		response.setOrderId(entity.getOrder().getId());
		response.setTrackingNumber(entity.getTrackingNumber());
		response.setCourierService(entity.getLovCourierService().getName());
		response.setStatusShipment(entity.getStatusShipment().getStatusString());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		return response;
	}

	private String generateTrackingNumber() {
		return "SHIP" + (int) (Math.random() * 100000);
	}

	@Override
	public Shipment changeStatusShipment(Shipment shipment) {
		return repository.save(shipment);
	}

	@Override
	public Optional<Shipment> getEntityByOrderId(Object value, Class<Shipment> entityType) {
		return repository.findByField(value, entityType);
	}

}
