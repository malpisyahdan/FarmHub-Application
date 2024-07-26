package com.project.app.farmhub.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.error.ErrorMessageConstant;
import com.project.app.farmhub.helper.SecurityHelper;
import com.project.app.farmhub.repository.ProductRepository;
import com.project.app.farmhub.request.CreateProductRequest;
import com.project.app.farmhub.request.UpdateProductRequest;
import com.project.app.farmhub.response.ProductResponse;
import com.project.app.farmhub.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;
	private final UserDetailsServiceImp userService;
	private static final String PROP_CODE = "code ";

	@Transactional
	@Override
	public void add(CreateProductRequest request) {
		validateRole("add");
		validateBkNotExist(request);
		Product entity = new Product();
		String currentUserId = SecurityHelper.getCurrentUserId();
		if (currentUserId == null) {
			throw new RuntimeException("Unable to get the current user ID.");
		}
		entity.setFarmer(userService.getEntityById(currentUserId).orElse(null));
		mapToEntity(entity, request);
		repository.save(entity);
	}

	private void validateRole(String action) {
		if (!SecurityHelper.hasRole("FARMER")) {
			throw new IllegalArgumentException("Only users with role FARMER can " + action + " products.");
		}
	}

	private void validateBkNotExist(CreateProductRequest request) {
		long count = repository.countByField(request.getCode(), Product.class);
		if (count > 0) {
			throw new RuntimeException(PROP_CODE + ErrorMessageConstant.IS_EXISTS);
		}
	}

	private void validateBkNotChange(UpdateProductRequest request, Product entity) {
		if (!request.getCode().equals(entity.getCode())) {
			throw new RuntimeException(PROP_CODE + ErrorMessageConstant.CANNOT_BE_CHANGE);
		}
	}

	private void mapToEntity(Product entity, CreateProductRequest request) {
		entity.setCode(request.getCode());
		entity.setName(request.getName());
		entity.setDescription(request.getDescription());
		entity.setPrice(request.getPrice());
		entity.setStock(request.getStock());
	}

	@Transactional
	@Override
	public void edit(UpdateProductRequest request) {
		validateRole("edit");
		getEntityById(request.getId()).ifPresentOrElse(entity -> {
			validateBkNotChange(request, entity);
			mapToEntity(entity, request);
			repository.save(entity);
		}, () -> {
			throw new RuntimeException("id " + ErrorMessageConstant.IS_NOT_EXISTS);
		});
	}

	@Transactional
	@Override
	public void delete(String id) {
		validateRole("delete");
		getEntityById(id).ifPresentOrElse(entity -> {
			repository.delete(entity);
		}, () -> {
			throw new RuntimeException("id " + ErrorMessageConstant.IS_NOT_EXISTS);
		});
	}

	@Override
	public Optional<Product> getEntityById(String id) {
		return repository.findById(id, Product.class);
	}

	@Override
	public ProductResponse getById(String id) {
		Product entity = getEntityById(id).orElseThrow(() -> new RuntimeException("id is not exist"));
		return mapToResponse(entity);
	}

	@Override
	public List<ProductResponse> getAll() {
		List<Product> detail = repository.findAll(Product.class);
		return detail.stream().map(this::mapToResponse).toList();
	}

	@Override
	public ProductResponse mapToResponse(Product entity) {
		ProductResponse response = new ProductResponse();
		response.setId(entity.getId());
		response.setCode(entity.getCode());
		response.setName(entity.getName());
		response.setDescription(entity.getDescription());
		response.setPrice(entity.getPrice());
		response.setStock(entity.getStock());
		response.setFarmerId(entity.getFarmer().getId());
		response.setFarmerName(entity.getFarmer().getUsername());
		response.setCreatedAt(entity.getCreatedAt());
		response.setUpdatedAt(entity.getUpdatedAt());
		response.setVersion(entity.getVersion());
		return response;
	}

	@Transactional
	@Override
	public Product changeTotalStock(Product product) {
		return repository.save(product);
	}
}
