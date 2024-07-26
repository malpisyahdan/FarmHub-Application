package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

public interface MasterRepository<T> {

	List<T> findAll(Class<T> entityType);

	Optional<T> findById(String id, Class<T> entityType);

	T save(T entity);

	void delete(T entity);

}
