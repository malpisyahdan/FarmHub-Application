package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class ProductRepository implements MasterRepository<Product> {

	@PersistenceContext
	private EntityManager entityManager;

	public long countByField(Object value, Class<Product> entityType) {
		String sql = "SELECT COUNT(*) FROM t_product WHERE code = :value";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("value", value);
		return ((Number) query.getSingleResult()).longValue();
	}

	@Override
	public List<Product> findAll(Class<Product> entityType) {
		String sql = "SELECT * FROM t_product";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<Product> findById(String id, Class<Product> entityType) {
		Product entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public Product save(Product entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Product entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}
	}

}
