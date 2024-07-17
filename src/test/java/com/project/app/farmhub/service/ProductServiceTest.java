package com.project.app.farmhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.ProductRepository;
import com.project.app.farmhub.request.CreateProductRequest;
import com.project.app.farmhub.request.UpdateProductRequest;
import com.project.app.farmhub.response.ProductResponse;
import com.project.app.farmhub.service.impl.ProductServiceImpl;

import jakarta.validation.ValidationException;

class ProductServiceTest {

	@Mock
	private ProductRepository repository;

	@Mock
	private UserDetailsServiceImp userService;

	@InjectMocks
	private ProductServiceImpl productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddProduct() {
		CreateProductRequest request = new CreateProductRequest();
		request.setCode("TIMUN");
		request.setName("Timun");
		request.setDescription("Fresh Timun");
		request.setPrice(BigDecimal.valueOf(10000.0));
		request.setStock(100);

		User currentUser = new User();
		currentUser.setId("1");
		currentUser.setUsername("Agus");

		try (MockedStatic<SecurityHelper> mockedStatic = mockStatic(SecurityHelper.class)) {
			mockedStatic.when(SecurityHelper::getCurrentUserId).thenReturn("1");

			when(userService.getEntityById("1")).thenReturn(Optional.of(currentUser));
			when(SecurityHelper.hasRole("FARMER")).thenReturn(true);
			when(repository.existsByCode("TIMUN")).thenReturn(false);

			productService.add(request);

			verify(repository, times(1)).saveAndFlush(any(Product.class));
		}
	}

	@Test
	void testAddProductUnauthorized() {
		CreateProductRequest request = new CreateProductRequest();

		try (MockedStatic<SecurityHelper> mockedStatic = Mockito.mockStatic(SecurityHelper.class)) {
			mockedStatic.when(SecurityHelper::getCurrentUserId).thenReturn("1");
			mockedStatic.when(() -> SecurityHelper.hasRole("FARMER")).thenReturn(false);

			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
				productService.add(request);
			});

			assertEquals("Only users with role FARMER can add products.", exception.getMessage());
		}
	}

	@Test
	void testAddProductWithExistingCode() {
		CreateProductRequest request = new CreateProductRequest();
		request.setCode("TIMUN");

		try (MockedStatic<SecurityHelper> mockedStatic = mockStatic(SecurityHelper.class)) {
			mockedStatic.when(SecurityHelper::getCurrentUserId).thenReturn("1");

			when(SecurityHelper.hasRole("FARMER")).thenReturn(true);
			when(repository.existsByCode("TIMUN")).thenReturn(true);

			ValidationException exception = assertThrows(ValidationException.class, () -> {
				productService.add(request);
			});

			assertEquals("code is exist", exception.getMessage());
		}
	}

	@Test
	void testEditProduct() {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setId("1");
		request.setCode("TIMUN");
		request.setName("Timun Hijau");
		request.setDescription("Fresh Timun Hijau");
		request.setPrice(BigDecimal.valueOf(15000.0));
		request.setStock(5);

		Product existingProduct = new Product();
		existingProduct.setId("1");
		existingProduct.setCode("TIMUN");

		when(repository.findById("1")).thenReturn(Optional.of(existingProduct));
		mockStatic(SecurityHelper.class);
		when(SecurityHelper.hasRole("FARMER")).thenReturn(true);

		productService.edit(request);

		verify(repository, times(1)).saveAndFlush(any(Product.class));
	}

	@Test
	void testEditProductWithChangedCode() {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setId("1");
		request.setCode("WORTEL");

		Product existingProduct = new Product();
		existingProduct.setId("1");
		existingProduct.setCode("TOMAT");

		when(repository.findById("1")).thenReturn(Optional.of(existingProduct));

		try (MockedStatic<SecurityHelper> mockedSecurityHelper = Mockito.mockStatic(SecurityHelper.class)) {
			mockedSecurityHelper.when(() -> SecurityHelper.hasRole("FARMER")).thenReturn(true);

			ValidationException exception = assertThrows(ValidationException.class, () -> {
				productService.edit(request);
			});

			assertEquals("code cannot be change", exception.getMessage());
		}
	}

	@Test
	void testDeleteProduct() {
		String productId = "1";
		Product existingProduct = new Product();
		existingProduct.setId("1");

		when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));

		productService.delete(productId);

		verify(repository, times(1)).deleteById(productId);
	}

	@Test
	void testDeleteNonExistentProduct() {
		String productId = "1";

		when(repository.findById(productId)).thenReturn(Optional.empty());

		try (MockedStatic<SecurityHelper> mockedSecurityHelper = Mockito.mockStatic(SecurityHelper.class)) {
			mockedSecurityHelper.when(() -> SecurityHelper.hasRole("FARMER")).thenReturn(true);

			ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
				productService.delete(productId);
			});

			assertEquals("id is not exist", exception.getReason());
		}
	}

	@Test
	void testGetById() {
		String productId = "1";

		User currentUser = new User();
		currentUser.setId("1");
		currentUser.setUsername("Agus");

		Product product = new Product();
		product.setId(productId);
		product.setCode("TIMUN");
		product.setName("Timun");
		product.setFarmer(currentUser);

		when(repository.findById(productId)).thenReturn(Optional.of(product));

		ProductResponse response = productService.getById(productId);

		assertNotNull(response);
		assertEquals(productId, response.getId());
		assertEquals("TIMUN", response.getCode());
		assertEquals("Timun", response.getName());
	}

	@Test
	void testGetAll() {

		User farmer = new User();
		farmer.setId("1");
		farmer.setUsername("Agus");

		Product product1 = new Product();
		product1.setId("1");
		product1.setCode("TIMUN");
		product1.setName("Timun");
		product1.setFarmer(farmer);

		Product product2 = new Product();
		product2.setId("2");
		product2.setCode("WORTEL");
		product2.setName("Wortel");
		product2.setFarmer(farmer);

		when(repository.findAll()).thenReturn(List.of(product1, product2));

		List<ProductResponse> responses = productService.getAll();

		assertNotNull(responses);
		assertEquals(2, responses.size());
	}

	@Test
	void testChangeTotalStock() {
		Product product = new Product();
		product.setId("1");
		product.setStock(20);

		when(repository.save(product)).thenReturn(product);

		Product updatedProduct = productService.changeTotalStock(product);

		assertNotNull(updatedProduct);
		assertEquals(20, updatedProduct.getStock());
	}
}
