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

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusShipment;
import com.project.app.farmhub.entity.LovData;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Shipment;
import com.project.app.farmhub.repository.ShipmentRepository;
import com.project.app.farmhub.request.CreateShipmentRequest;
import com.project.app.farmhub.response.ShipmentResponse;
import com.project.app.farmhub.service.impl.ShipmentServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

	@Mock
	private ShipmentRepository shipmentRepository;

	@Mock
	private OrderService orderService;
	
	@Mock
	private LovDataService lovService;

	@InjectMocks
	private ShipmentServiceImpl shipmentService;

	private Shipment shipment;
	private CreateShipmentRequest createShipmentRequest;

	@BeforeEach
	public void setUp() {
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
		shipment.setLovStatusShipment(lov);
		shipment.setStatusShipment(StatusShipment.SHIPPED);

		createShipmentRequest = new CreateShipmentRequest();
		createShipmentRequest.setOrderId("order123");
		createShipmentRequest.setLovCourierServiceId("lov3");
	}

	@Test
	void testAddShipment() {
		when(orderService.getEntityById(anyString())).thenReturn(Optional.of(shipment.getOrder()));
		when(lovService.getEntityById(anyString())).thenReturn(Optional.of(shipment.getLovStatusShipment()));

		when(shipmentRepository.saveAndFlush(any(Shipment.class))).thenReturn(shipment);

		shipmentService.add(createShipmentRequest);

		verify(shipmentRepository, times(1)).saveAndFlush(any(Shipment.class));
	}

	@Test
	void testAddShipmentOrderNotFound() {
		when(orderService.getEntityById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			shipmentService.add(createShipmentRequest);
		});
	}

	@Test
	void testDeleteShipment() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.of(shipment));

		shipmentService.delete("shipment123");

		verify(shipmentRepository, times(1)).deleteById("shipment123");
	}

	@Test
	void testDeleteShipmentNotFound() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			shipmentService.delete("shipment123");
		});
	}

	@Test
	void testGetEntityById() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.of(shipment));

		Optional<Shipment> result = shipmentService.getEntityById("shipment123");

		assertTrue(result.isPresent());
		assertEquals(shipment.getId(), result.get().getId());
		assertEquals(shipment.getOrder().getId(), result.get().getOrder().getId());
	}

	@Test
	void testGetEntityByIdNotFound() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.empty());

		Optional<Shipment> result = shipmentService.getEntityById("shipment123");

		assertTrue(result.isEmpty());
	}

	@Test
	void testGetById() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.of(shipment));

		ShipmentResponse response = shipmentService.getById("shipment123");

		assertNotNull(response);
		assertEquals("shipment123", response.getId());
		assertEquals("order123", response.getOrderId());
		assertEquals("SHIP12345", response.getTrackingNumber());
		assertEquals("JNE", response.getCourierService());
		assertEquals("SHIPPED", response.getStatusShipment());
	}

	@Test
	void testGetByIdNotFound() {
		when(shipmentRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			shipmentService.getById("shipment123");
		});
	}

	@Test
	void testGetAllShipments() {
		List<Shipment> shipments = new ArrayList<>();
		shipments.add(shipment);
		when(shipmentRepository.findAll()).thenReturn(shipments);

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

}
