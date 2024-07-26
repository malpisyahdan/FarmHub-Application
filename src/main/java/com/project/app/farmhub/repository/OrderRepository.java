package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class OrderRepository implements MasterRepository<Order> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Order> findAll(Class<Order> entityType) {
		String sql = "SELECT * FROM t_order";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<Order> findById(String id, Class<Order> entityType) {
		Order entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public Order save(Order entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Order entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}
	}

}
