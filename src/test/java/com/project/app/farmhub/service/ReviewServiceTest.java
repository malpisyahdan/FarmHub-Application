package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.entity.Review;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.error.ConstraintValidationException;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.MasterRepository;
import com.project.app.farmhub.request.CreateReviewRequest;
import com.project.app.farmhub.request.UpdateReviewRequest;
import com.project.app.farmhub.response.ReviewResponse;
import com.project.app.farmhub.service.impl.ReviewServiceImpl;

class ReviewServiceTest {

	@Mock
	private MasterRepository<Review, String> repository;

	@Mock
	private UserDetailsServiceImp userService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void addReview() {
		CreateReviewRequest request = new CreateReviewRequest();
		request.setProductId("productId");
		request.setRating(90);
		request.setComment("Great product!");

		User currentUser = new User();
		currentUser.setId("1");
		currentUser.setUsername("Agus");

		try (MockedStatic<SecurityHelper> mockedStatic = mockStatic(SecurityHelper.class)) {
			mockedStatic.when(SecurityHelper::getCurrentUserId).thenReturn("1");

			when(productService.getEntityById("productId")).thenReturn(Optional.of(new Product()));
			when(userService.getEntityById("1")).thenReturn(Optional.of(currentUser));
			when(SecurityHelper.hasRole("FARMER")).thenReturn(true);

			reviewService.add(request);

			verify(repository, times(1)).save(any(Review.class));
		}
	}

	@Test
	void addReviewProductNotExists() {
		CreateReviewRequest request = new CreateReviewRequest();
		request.setProductId("invalidProductId");
		request.setRating(90);
		request.setComment("Great product!");

		when(productService.getEntityById("invalidProductId")).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			reviewService.add(request);
		});

		assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) exception).getStatusCode());
	}

	@Test
	void editReview() {
		UpdateReviewRequest request = new UpdateReviewRequest();
		request.setId("reviewId");
		request.setProductId("productId");
		request.setRating(85);
		request.setComment("Updated comment");

		Review existingReview = new Review();
		when(repository.findById("reviewId", Review.class)).thenReturn(Optional.of(existingReview));
		when(productService.getEntityById("productId")).thenReturn(Optional.of(new Product()));

		assertDoesNotThrow(() -> reviewService.edit(request));
		verify(repository, times(1)).save(existingReview);
	}

	@Test
	void editReviewNotExists() {
		UpdateReviewRequest request = new UpdateReviewRequest();
		request.setId("invalidReviewId");
		request.setProductId("productId");
		request.setRating(85);
		request.setComment("Updated comment");

		when(repository.findById("invalidReviewId", Review.class)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ConstraintValidationException.class, () -> {
			reviewService.edit(request);
		});

		assertEquals(", id: [" + ErrorMessageConstant.IS_NOT_EXISTS + "]", exception.getMessage());
	}

	@Test
	void deleteReview() {
		String reviewId = "reviewId";
		Review review = new Review();

		when(repository.findById(reviewId, Review.class)).thenReturn(Optional.of(review));

		assertDoesNotThrow(() -> reviewService.delete(reviewId));
		verify(repository, times(1)).delete(review);
	}

	@Test
	void deleteReviewNotExists() {
		String reviewId = "invalidReviewId";

		when(repository.findById(reviewId, Review.class)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			reviewService.delete(reviewId);
		});

		assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) exception).getStatusCode());
	}

	@Test
	void getById() {
		String reviewId = "reviewId";
		Review review = new Review();
		review.setId(reviewId);
		review.setProduct(new Product());
		review.setUmkm(new User());

		when(repository.findById(reviewId, Review.class)).thenReturn(Optional.of(review));

		ReviewResponse response = reviewService.getById(reviewId);

		assertNotNull(response);
		assertEquals(reviewId, response.getId());
	}

	@Test
	void getByIdNotExists() {
		String reviewId = "invalidReviewId";

		when(repository.findById(reviewId, Review.class)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			reviewService.getById(reviewId);
		});

		assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException) exception).getStatusCode());
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
		when(repository.findAll(Review.class)).thenReturn(reviews);

		List<ReviewResponse> responses = reviewService.getAll();

		assertNotNull(responses);
		assertEquals(1, responses.size());

		ReviewResponse response = responses.get(0);
		assertEquals("review123", response.getId());
	}
}
