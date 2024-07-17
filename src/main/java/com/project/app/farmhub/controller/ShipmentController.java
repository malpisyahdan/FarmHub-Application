package com.project.app.farmhub.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.ResponseHelper;
import com.project.app.farmhub.request.CreateShipmentRequest;
import com.project.app.farmhub.response.ShipmentResponse;
import com.project.app.farmhub.response.WebResponse;
import com.project.app.farmhub.service.ShipmentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/v1/farmhub" })
public class ShipmentController {

	private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

	private final ShipmentService service;
	private static final String PROP_SHIPMENT = "shipment ";

	@PostMapping(value = "/shipment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> add(@RequestBody CreateShipmentRequest request) {
		logger.info("Add method called with request: {}", request);
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_SHIPMENT + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/shipment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_SHIPMENT + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/shipment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<ShipmentResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/shipment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<ShipmentResponse>>> getAll() {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAll()));
	}

}
