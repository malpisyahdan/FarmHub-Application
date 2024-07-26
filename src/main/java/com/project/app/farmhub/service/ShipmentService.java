package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Shipment;
import com.project.app.farmhub.request.CreateShipmentRequest;
import com.project.app.farmhub.response.ShipmentResponse;

public interface ShipmentService {

	void add(CreateShipmentRequest request);

	void delete(String id);

	Optional<Shipment> getEntityById(String id);

	ShipmentResponse getById(String id);

	List<ShipmentResponse> getAll();

	Shipment changeStatusShipment(Shipment shipment);
	
	Optional<Shipment> getEntityByOrderId(Object value, Class<Shipment> entityType);

}
