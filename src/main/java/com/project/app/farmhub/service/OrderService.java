package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.request.CancelOrderRequest;
import com.project.app.farmhub.request.CreateOrderRequest;
import com.project.app.farmhub.response.OrderResponse;

public interface OrderService {

	void add(CreateOrderRequest request);

	void delete(String id);

	Optional<Order> getEntityById(String id);

	OrderResponse getById(String id);

	List<OrderResponse> getAll();

	Order changeStatusPayment(Order order);

	Order changeStatus(Order order);

	void cancelOrder(CancelOrderRequest request);

}
