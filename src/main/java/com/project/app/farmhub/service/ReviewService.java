package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Review;
import com.project.app.farmhub.request.CreateReviewRequest;
import com.project.app.farmhub.request.UpdateReviewRequest;
import com.project.app.farmhub.response.ReviewResponse;

public interface ReviewService {

	void add(CreateReviewRequest request);

	void edit(UpdateReviewRequest request);

	void delete(String id);

	Optional<Review> getEntityById(String id);

	ReviewResponse getById(String id);

	List<ReviewResponse> getAll();

}
