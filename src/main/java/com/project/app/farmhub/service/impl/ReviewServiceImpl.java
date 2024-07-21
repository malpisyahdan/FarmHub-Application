package com.project.app.farmhub.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.entity.Review;
import com.project.app.farmhub.error.ConstraintValidationException;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.MasterRepository;
import com.project.app.farmhub.request.CreateReviewRequest;
import com.project.app.farmhub.request.UpdateReviewRequest;
import com.project.app.farmhub.response.ReviewResponse;
import com.project.app.farmhub.service.ProductService;
import com.project.app.farmhub.service.ReviewService;
import com.project.app.farmhub.service.UserDetailsServiceImp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final MasterRepository<Review, String> repository;
	private final UserDetailsServiceImp userService;
	private final ProductService productService;

	private static final String PROP_PRODUCT = "product ";

	@Transactional
	@Override
	public void add(CreateReviewRequest request) {
		validateNonBk(request);
		Review entity = new Review();
		String currentUserId = SecurityHelper.getCurrentUserId();
		if (currentUserId == null) {
			throw new IllegalArgumentException("Unable to get the current user ID.");
		}
		entity.setUmkm(userService.getEntityById(currentUserId).orElse(null));
		mapToEntity(entity, request);
		repository.save(entity);

	}

	private void validateNonBk(CreateReviewRequest request) {

		productService.getEntityById(request.getProductId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						PROP_PRODUCT + ErrorMessageConstant.IS_NOT_EXISTS));

	}

	private void mapToEntity(Review entity, CreateReviewRequest request) {
		if (request.getRating() > 100) {
			throw new IllegalArgumentException("Rating cannot be more than 100");
		}
		entity.setProduct(productService.getEntityById(request.getProductId()).orElse(null));
		entity.setRating(request.getRating());
		entity.setComment(request.getComment());

	}

	@Transactional
	@Override
	public void edit(UpdateReviewRequest request) {
		getEntityById(request.getId()).ifPresentOrElse(entity -> {
			validateNonBk(request);
			mapToEntity(entity, request);
			repository.save(entity);
		}, () -> {
			throw new ConstraintValidationException("id", ErrorMessageConstant.IS_NOT_EXISTS);
		});

	}
	
	@Transactional
	@Override
	public void delete(String id) {
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.delete(entity);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is not exist");
		});

	}

	@Override
	public Optional<Review> getEntityById(String id) {
		return repository.findById(id, Review.class);
	}

	@Override
	public ReviewResponse getById(String id) {
		Review entity = getEntityById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "id" + " is not exist"));
		return mapToResponse(entity);
	}

	@Override
	public List<ReviewResponse> getAll() {
		List<Review> detail = repository.findAll(Review.class);
		return detail.stream().map(this::mapToResponse).toList();
	}

	private ReviewResponse mapToResponse(Review entity) {
		ReviewResponse response = new ReviewResponse();
		response.setId(entity.getId());
		response.setProductId(entity.getProduct().getId());
		response.setProductCode(entity.getProduct().getCode());
		response.setProductName(entity.getProduct().getName());
		response.setRating(entity.getRating());
		response.setComment(entity.getComment());
		response.setUmkmId(entity.getUmkm().getId());
		response.setUmkmName(entity.getUmkm().getUsername());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		return response;
	}

}
