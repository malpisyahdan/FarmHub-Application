package com.project.app.farmhub.service.impl;

import org.springframework.stereotype.Service;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusShipment;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Shipment;
import com.project.app.farmhub.request.ConfirmDeliverOrderRequest;
import com.project.app.farmhub.service.DeliverService;
import com.project.app.farmhub.service.OrderService;
import com.project.app.farmhub.service.ShipmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliverServiceImpl implements DeliverService{
	
	private final OrderService orderService;
	private final ShipmentService shipmentService;
	
	@Transactional
	@Override
	public void confirmDeliver(ConfirmDeliverOrderRequest request) {

		Order order = orderService.getEntityById(request.getId())
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + request.getId()));
		
		if (order.getStatus() != StatusOrder.SHIPPED) {
			throw new IllegalArgumentException("Only orders with status SHIPPED can be confirmed as DELIVERED");
		}
		
		order.setStatus(StatusOrder.DELIVERED);
		orderService.changeStatus(order);

		Shipment shipment = shipmentService.getEntityByOrderId(request.getId(), Shipment.class)
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + request.getId()));

		shipment.setStatusShipment(StatusShipment.DELIVERED);
		shipmentService.changeStatusShipment(shipment);

	}

}
