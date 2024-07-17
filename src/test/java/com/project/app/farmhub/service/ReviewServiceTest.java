package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.entity.Review;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.error.ConstraintValidationException;
import com.project.app.farmhub.repository.ReviewRepository;
import com.project.app.farmhub.request.CreateReviewRequest;
import com.project.app.farmhub.request.UpdateReviewRequest;
import com.project.app.farmhub.response.ReviewResponse;
import com.project.app.farmhub.service.impl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private UserDetailsServiceImp userDetailsService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	private Review review;
	private CreateReviewRequest createReviewRequest;
	private UpdateReviewRequest updateReviewRequest;

	@BeforeEach
	public void setUp() {
		review = new Review();
		review.setId("review123");

		createReviewRequest = new CreateReviewRequest();
		createReviewRequest.setProductId("product123");
		createReviewRequest.setRating(90);
		createReviewRequest.setComment("Good product");

		updateReviewRequest = new UpdateReviewRequest();
		updateReviewRequest.setId("review123");
		updateReviewRequest.setProductId("product123");
		updateReviewRequest.setRating(85);
		updateReviewRequest.setComment("Updated comment");

		Mockito.lenient().when(userDetailsService.getEntityById(anyString())).thenReturn(Optional.empty());
	}

	@Test
	void testAddReview() {
		when(productService.getEntityById(anyString())).thenReturn(Optional.of(new Product()));

		when(userDetailsService.getEntityById(anyString())).thenReturn(Optional.of(new User()));

		when(reviewRepository.saveAndFlush(any(Review.class))).thenReturn(review);

		reviewService.add(createReviewRequest);

		verify(reviewRepository, times(1)).saveAndFlush(any(Review.class));
	}

	@Test
	void testAddReviewProductNotFound() {
		when(productService.getEntityById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			reviewService.add(createReviewRequest);
		});
	}

	@Test
	void testEditReview() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.of(review));

		when(productService.getEntityById(anyString())).thenReturn(Optional.of(new Product()));

		reviewService.edit(updateReviewRequest);

		verify(reviewRepository, times(1)).saveAndFlush(any(Review.class));
	}

	@Test
	void testEditReviewNotFound() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ConstraintValidationException.class, () -> {
			reviewService.edit(updateReviewRequest);
		});
	}

	@Test
	void testDeleteReview() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.of(review));

		reviewService.delete("review123");

		verify(reviewRepository, times(1)).deleteById("review123");
	}

	@Test
	void testDeleteReviewNotFound() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			reviewService.delete("review123");
		});
	}

	@Test
	void testGetEntityById() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.of(review));

		Optional<Review> result = reviewService.getEntityById("review123");

		assertTrue(result.isPresent());
		assertEquals(review.getId(), result.get().getId());
	}

	@Test
	void testGetEntityByIdNotFound() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.empty());

		Optional<Review> result = reviewService.getEntityById("review123");

		assertTrue(result.isEmpty());
	}

	@Test
	void testGetById() {

		String reviewId = "review123";

		User umkm = new User();
		umkm.setId("1");
		umkm.setUsername("Agus");

		Product product = new Product();
		product.setId("1");
		product.setCode("TIMUN");
		product.setName("Timun");

		Review rev = new Review();
		rev.setId(reviewId);
		rev.setUmkm(umkm);
		rev.setProduct(product);
		rev.setComment("Bagus Buahnya");
		rev.setRating(99);
		when(reviewRepository.findById(anyString())).thenReturn(Optional.of(rev));

		ReviewResponse response = reviewService.getById("review123");

		assertNotNull(response);
		assertEquals("review123", response.getId());
	}

	@Test
	void testGetByIdNotFound() {
		when(reviewRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(ResponseStatusException.class, () -> {
			reviewService.getById("review123");
		});
	}

	@Test
	void testGetAllReviews() {

		String reviewId = "review123";

		User umkm = new User();
		umkm.setId("1");
		umkm.setUsername("Agus");

		Product product = new Product();
		product.setId("1");
		product.setCode("TIMUN");
		product.setName("Timun");

		Review rev = new Review();
		rev.setId(reviewId);
		rev.setUmkm(umkm);
		rev.setProduct(product);
		rev.setComment("Bagus Buahnya");
		rev.setRating(99);

		List<Review> reviews = new ArrayList<>();

		reviews.add(rev);
		when(reviewRepository.findAll()).thenReturn(reviews);

		List<ReviewResponse> responses = reviewService.getAll();

		assertNotNull(responses);
		assertEquals(1, responses.size());

		ReviewResponse response = responses.get(0);
		assertEquals("review123", response.getId());
	}

}
