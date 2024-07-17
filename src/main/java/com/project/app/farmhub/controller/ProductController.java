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
import com.project.app.farmhub.request.CreateProductRequest;
import com.project.app.farmhub.request.UpdateProductRequest;
import com.project.app.farmhub.response.ProductResponse;
import com.project.app.farmhub.response.WebResponse;
import com.project.app.farmhub.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/v1/farmhub" })
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService service;
	private static final String PROP_PRODUCT = "product ";

	@PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> add(@RequestBody CreateProductRequest request) {
		logger.info("Add method called with request: {}", request);
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_PRODUCT + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateProductRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_PRODUCT + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_PRODUCT + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<ProductResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<ProductResponse>>> getAll() {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAll()));
	}

}
