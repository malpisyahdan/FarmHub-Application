package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class UserRepository implements MasterRepository<User> {

	@PersistenceContext
	private EntityManager entityManager;

	public Optional<User> findByField(Object value, Class<User> entityType) {
		String sql = "SELECT * FROM t_users WHERE username = :value";
		Query query = entityManager.createNativeQuery(sql, entityType);
		query.setParameter("value", value);
		List<?> results = query.getResultList();
		List<User> user = results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
		return user.stream().findFirst();
	}

	@Override
	public List<User> findAll(Class<User> entityType) {
		String sql = "SELECT * FROM t_users";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<User> findById(String id, Class<User> entityType) {
		User entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public User save(User entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(User entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}

	}

}
