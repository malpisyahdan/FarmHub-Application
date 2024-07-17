package com.project.app.farmhub.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.common.type.StatusOrder;
import com.project.app.farmhub.common.type.StatusPayment;
import com.project.app.farmhub.entity.Order;
import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.error.ConstraintValidationException;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.OrderRepository;
import com.project.app.farmhub.request.CancelOrderRequest;
import com.project.app.farmhub.request.CreateOrderRequest;
import com.project.app.farmhub.response.OrderResponse;
import com.project.app.farmhub.service.OrderService;
import com.project.app.farmhub.service.ProductService;
import com.project.app.farmhub.service.UserDetailsServiceImp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;
	private final UserDetailsServiceImp userService;
	private final ProductService productService;

	private static final String PROP_PRODUCT = "product ";

	@Transactional
	@Override
	public void add(CreateOrderRequest request) {
		validateNonBk(request);
		validateProductStock(request);
		Order entity = new Order();
		String currentUserId = SecurityHelper.getCurrentUserId();
		if (currentUserId == null) {
			throw new IllegalArgumentException("Unable to get the current user ID.");
		}
		entity.setUmkm(userService.getEntityById(currentUserId).orElse(null));
		mapToEntity(entity, request);
		repository.saveAndFlush(entity);

	}

	private void validateNonBk(CreateOrderRequest request) {

		productService.getEntityById(request.getProductId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						PROP_PRODUCT + ErrorMessageConstant.IS_NOT_EXISTS));

	}

	private void validateProductStock(CreateOrderRequest request) {
		Optional<Product> productOpt = productService.getEntityById(request.getProductId());
		if (productOpt.isEmpty()) {
			throw new ConstraintValidationException(null,
					Map.ofEntries(Map.entry("productId", Collections.singletonList("Product tidak ditemukan"))));
		}

		Product product = productOpt.get();

		if (product.getStock() < request.getQuantity()) {
			throw new ConstraintValidationException(null,
					Map.ofEntries(Map.entry("quantity", Collections.singletonList("Stock product tidak mencukupi"))));
		}
	}

	private void mapToEntity(Order entity, CreateOrderRequest request) {

		String farmer = productService.getById(request.getProductId()).getFarmerId();
		entity.setFarmer(userService.getEntityById(farmer).orElse(null));
		entity.setQuantity(request.getQuantity());

		Product product = productService.getEntityById(request.getProductId()).orElseThrow(
				() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

		entity.setProduct(product);

		BigDecimal productPrice = productService.getById(request.getProductId()).getPrice();

		BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());

		BigDecimal totalPrice = productPrice.multiply(quantity);

		entity.setTotalPrice(totalPrice);

		product.setStock(product.getStock() - request.getQuantity());
		productService.changeTotalStock(product);

		entity.setStatus(StatusOrder.PENDING);
		entity.setStatusPayment(StatusPayment.PENDING);

	}

	@Override
	public void delete(String id) {
		repository.findById(id).ifPresentOrElse(entity -> repository.deleteById(id), () -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is not exist");
		});

	}

	@Override
	public Optional<Order> getEntityById(String id) {
		return repository.findById(id);
	}

	@Override
	public OrderResponse getById(String id) {
		Order entity = getEntityById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "id" + " is not exist"));
		return mapToResponse(entity);
	}

	@Override
	public List<OrderResponse> getAll() {
		List<Order> detail = repository.findAll();
		return detail.stream().map(this::mapToResponse).toList();
	}

	private OrderResponse mapToResponse(Order entity) {
		OrderResponse response = new OrderResponse();
		response.setId(entity.getId());

		response.setTotalPrice(entity.getTotalPrice());
		response.setQuantity(entity.getQuantity());
		response.setFarmerId(entity.getFarmer().getId());
		response.setFarmerName(entity.getFarmer().getUsername());
		response.setUmkmId(entity.getUmkm().getId());
		response.setUmkmName(entity.getUmkm().getUsername());
		response.setProductId(entity.getProduct().getId());
		response.setProductName(entity.getProduct().getName());
		response.setProductPrice(entity.getProduct().getPrice());
		response.setStatus(entity.getStatus().getStatusString());
		response.setStatusPayment(entity.getStatusPayment().getStatusString());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		return response;
	}

	@Override
	public Order changeStatusPayment(Order order) {
		return repository.save(order);
	}

	@Override
	public Order changeStatus(Order order) {
		return repository.save(order);
	}

	@Transactional
	@Override
	public void cancelOrder(CancelOrderRequest request) {
		Order order = getEntityById(request.getId())
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + request.getId()));

		if (order.getStatus() != StatusOrder.PENDING) {
			throw new IllegalArgumentException("Only orders with status PENDING can be canceled");
		}

		order.setStatus(StatusOrder.CANCELLED);

		Product product = productService.getEntityById(order.getProduct().getId()).orElseThrow(
				() -> new IllegalArgumentException("Product not found with id: " + order.getProduct().getId()));

		product.setStock(product.getStock() + order.getQuantity());

		repository.save(order);
		productService.changeTotalStock(product);

	}

}
