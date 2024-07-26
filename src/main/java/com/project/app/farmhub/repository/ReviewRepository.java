package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Review;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class ReviewRepository implements MasterRepository<Review> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Review> findAll(Class<Review> entityType) {
		String sql = "SELECT * FROM t_review";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<Review> findById(String id, Class<Review> entityType) {
		Review entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public Review save(Review entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Review entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}

	}

}
