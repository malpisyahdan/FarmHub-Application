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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.ResponseHelper;
import com.project.app.farmhub.request.CreateLovDataRequest;
import com.project.app.farmhub.request.UpdateLovDataRequest;
import com.project.app.farmhub.response.LovDataResponse;
import com.project.app.farmhub.response.WebResponse;
import com.project.app.farmhub.service.LovDataService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/v1/farmhub" })
public class LovDataController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final LovDataService service;
	private static final String PROP_LOV = "lov data ";

	@PostMapping(value = "/lov-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> add(@RequestBody CreateLovDataRequest request) {
		logger.info("Add method called with request: {}", request);
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_LOV + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/lov-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateLovDataRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_LOV + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/lov-data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_LOV + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/lov-data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<LovDataResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/lov-data", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<LovDataResponse>>> getAll() {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAll()));
	}

	@GetMapping(value = "/lovs-data/payment-method", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<LovDataResponse>>> getLovPaymentMethod(Boolean isActive) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getLovs("PAYMENT-METHOD", isActive)));
	}

	@GetMapping(value = "/lovs-data/courier-service", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<LovDataResponse>>> getLovCourierService(Boolean isActive) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getLovs("COURIER-SERVICE", isActive)));
	}

}
