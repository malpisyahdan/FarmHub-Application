package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class MasterRepositoryImpl<T, ID> implements MasterRepository<T, ID> {

	@PersistenceContext
	private EntityManager entityManager;

	protected String getTableName(Class<T> entityType) {
		Table table = entityType.getAnnotation(Table.class);

		return table.name();

	}

	@Override
	public long countByField(String fieldName, Object value, Class<T> entityType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<T> root = criteriaQuery.from(entityType);

		criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(root.get(fieldName), value));

		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	@Override
	public Optional<T> findByField(String fieldName, Object value, Class<T> entityType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityType);

		Root<T> root = criteriaQuery.from(entityType);

		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(fieldName), value));

		try {
			T result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<T> findAll(Class<T> entityType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityType);

		Root<T> root = criteriaQuery.from(entityType);

		criteriaQuery.select(root);

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public Optional<T> findById(ID id, Class<T> entityType) {
		T entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	public T save(T entity) {
		entityManager.persist(entity);
		entityManager.flush();
		return entity;
	}

	@Override
	public void delete(T entity) {
		entityManager.remove(entity);
		entityManager.flush();
	}

}