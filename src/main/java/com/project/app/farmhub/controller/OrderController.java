package com.project.app.farmhub.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.ResponseHelper;
import com.project.app.farmhub.request.CancelOrderRequest;
import com.project.app.farmhub.request.ConfirmDeliverOrderRequest;
import com.project.app.farmhub.request.CreateOrderRequest;
import com.project.app.farmhub.response.OrderResponse;
import com.project.app.farmhub.response.WebResponse;
import com.project.app.farmhub.service.DeliverService;
import com.project.app.farmhub.service.OrderService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/farmhub" })
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private final OrderService service;
	private final DeliverService deliveryService;
	private static final String PROP_ORDER = "order ";

	@PostMapping(value = "/order")
	public ResponseEntity<WebResponse<String>> add(@Valid @RequestBody CreateOrderRequest request) {
		logger.info("Add method called with request: {}", request);
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ORDER + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/order/{id}")
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ORDER + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/order/{id}")
	public ResponseEntity<WebResponse<OrderResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/order")
	public ResponseEntity<WebResponse<List<OrderResponse>>> getAll() {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAll()));
	}

	@PutMapping(value = "/order/confirm-deliver")
	public ResponseEntity<WebResponse<String>> confirmDeliver(@RequestBody ConfirmDeliverOrderRequest request) {
		deliveryService.confirmDeliver(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ORDER + ErrorMessageConstant.HAS_BEEN_CONFIRM_SUCCESSFULLY));
	}

	@PutMapping(value = "/order/cancel-order")
	public ResponseEntity<WebResponse<String>> confirmDeliver(@Valid @RequestBody CancelOrderRequest request) {
		service.cancelOrder(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_ORDER + ErrorMessageConstant.HAS_BEEN_CANCEL_SUCCESSFULLY));
	}

}
