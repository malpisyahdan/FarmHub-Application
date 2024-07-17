package com.project.app.farmhub.service;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Product;
import com.project.app.farmhub.request.CreateProductRequest;
import com.project.app.farmhub.request.UpdateProductRequest;
import com.project.app.farmhub.response.ProductResponse;

public interface ProductService {

	void add(CreateProductRequest request);

	void edit(UpdateProductRequest request);

	void delete(String id);

	Optional<Product> getEntityById(String id);

	ProductResponse getById(String id);

	List<ProductResponse> getAll();
	
	Product changeTotalStock(Product product);
	
	ProductResponse mapToResponse(Product entity);

}
