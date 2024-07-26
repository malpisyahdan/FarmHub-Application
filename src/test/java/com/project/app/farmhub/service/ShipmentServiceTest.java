package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusShipment;
import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Shipment;
import com.project.app.farmhub.repository.ShipmentRepository;
import com.project.app.farmhub.request.CreateShipmentRequest;
import com.project.app.farmhub.response.ShipmentResponse;
import com.project.app.farmhub.service.impl.ShipmentServiceImpl;

class ShipmentServiceTest {

	@Mock
	private ShipmentRepository repository;

	@Mock
	private OrderService orderService;

	@Mock
	private LovDataService lovService;

	@InjectMocks
	private ShipmentServiceImpl shipmentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void addShipment() {
		CreateShipmentRequest request = new CreateShipmentRequest();
		request.setOrderId("orderId");
		request.setLovCourierServiceId("lovCourierServiceId");

		Order order = new Order();
		LovData lovData = new LovData();

		when(orderService.getEntityById("orderId")).thenReturn(Optional.of(order));
		when(lovService.getEntityById("lovCourierServiceId")).thenReturn(Optional.of(lovData));

		assertDoesNotThrow(() -> shipmentService.add(request));
		verify(repository, times(1)).save(any(Shipment.class));
		verify(orderService, times(1)).changeStatusPayment(any(Order.class));
	}

	@Test
	void addShipmentOrderNotExists() {
		CreateShipmentRequest request = new CreateShipmentRequest();
		request.setOrderId("invalidOrderId");
		request.setLovCourierServiceId("lovCourierServiceId");

		when(orderService.getEntityById("invalidOrderId")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			shipmentService.add(request);
		});

		assertEquals("order is not exists", exception.getMessage());
	}

	@Test
	void deleteShipment() {
		String shipmentId = "shipmentId";
		Shipment shipment = new Shipment();

		when(repository.findById(shipmentId, Shipment.class)).thenReturn(Optional.of(shipment));

		assertDoesNotThrow(() -> shipmentService.delete(shipmentId));
		verify(repository, times(1)).delete(shipment);
	}

	@Test
	void deleteShipmentNotExists() {
		String shipmentId = "invalidShipmentId";

		when(repository.findById(shipmentId, Shipment.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			shipmentService.delete(shipmentId);
		});

		assertEquals("id is not exist", exception.getMessage());
	}

	@Test
	void getById() {
		String shipmentId = "shipmentId";
		Shipment shipment = new Shipment();
		shipment.setId(shipmentId);
		shipment.setOrder(new Order());
		shipment.setLovCourierService(new LovData());
		shipment.setStatusShipment(StatusShipment.SHIPPED);

		when(repository.findById(shipmentId, Shipment.class)).thenReturn(Optional.of(shipment));

		ShipmentResponse response = shipmentService.getById(shipmentId);

		assertNotNull(response);
		assertEquals(shipmentId, response.getId());
	}

	@Test
	void getByIdShipmentNotExists() {
		String shipmentId = "invalidShipmentId";

		when(repository.findById(shipmentId, Shipment.class)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			shipmentService.getById(shipmentId);
		});

		assertEquals("id is not exist", exception.getMessage());
	}

	@Test
	void getAllShipments() {

		Shipment shipment = new Shipment();
		Order order = new Order();
		order.setId("order123");
		order.setStatus(StatusOrder.PENDING);

		LovData lov = new LovData();
		lov.setId("lov3");
		lov.setCode("JNE");
		lov.setName("JNE");
		lov.setLovType("COURIER-SERVICE");

		shipment = new Shipment();
		shipment.setId("shipment123");
		shipment.setOrder(order);
		shipment.setTrackingNumber("SHIP12345");
		shipment.setLovCourierService(lov);
		shipment.setStatusShipment(StatusShipment.SHIPPED);

		List<Shipment> shipments = new ArrayList<>();
		shipments.add(shipment);
		when(repository.findAll(Shipment.class)).thenReturn(shipments);

		List<ShipmentResponse> responses = shipmentService.getAll();

		assertNotNull(responses);
		assertEquals(1, responses.size());

		ShipmentResponse response = responses.get(0);
		assertEquals("shipment123", response.getId());
		assertEquals("order123", response.getOrderId());
		assertEquals("SHIP12345", response.getTrackingNumber());
		assertEquals("JNE", response.getCourierService());
		assertEquals("SHIPPED", response.getStatusShipment());
	}

	@Test
	void changeStatusShipment() {
		Shipment shipment = new Shipment();
		shipment.setStatusShipment(StatusShipment.DELIVERED);

		when(repository.save(any(Shipment.class))).thenReturn(shipment);

		Shipment updatedShipment = shipmentService.changeStatusShipment(shipment);

		assertEquals(StatusShipment.DELIVERED, updatedShipment.getStatusShipment());
	}
}
