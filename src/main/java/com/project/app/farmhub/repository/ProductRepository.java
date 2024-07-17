package com.project.app.farmhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.app.farmhub.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

	boolean existsByCode(String code);
	
	 Optional<Product> findByCode(String code);

}