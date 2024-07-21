package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

public interface MasterRepository<T, ID> {

	long countByField(String fieldName, Object value, Class<T> entityType);

	Optional<T> findByField(String fieldName, Object value, Class<T> entityType);

	List<T> findAll(Class<T> entityType);

	Optional<T> findById(ID id, Class<T> entityType);

	T save(T entity);
	
	void delete(T entity);
	
	List<T> saveAll(List<T> entities);
}
