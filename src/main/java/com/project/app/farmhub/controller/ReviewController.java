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
import com.project.app.farmhub.request.CreateReviewRequest;
import com.project.app.farmhub.request.UpdateReviewRequest;
import com.project.app.farmhub.response.ReviewResponse;
import com.project.app.farmhub.response.WebResponse;
import com.project.app.farmhub.service.ReviewService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/v1/farmhub" })
public class ReviewController {

	private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

	private final ReviewService service;
	private final String PROP_REVIEW = "review ";

	@PostMapping(value = "/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> add(@RequestBody CreateReviewRequest request) {
		logger.info("Add method called with request: {}", request);
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_REVIEW + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateReviewRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_REVIEW + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/review/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_REVIEW + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/review/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<ReviewResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WebResponse<List<ReviewResponse>>> getAll() {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAll()));
	}

}
