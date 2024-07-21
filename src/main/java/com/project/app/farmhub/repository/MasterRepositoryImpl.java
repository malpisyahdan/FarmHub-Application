package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

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
		String query = "SELECT COUNT(*) FROM " + getTableName(entityType) + " WHERE " + fieldName + " = :value";
		return ((Number) entityManager.createNativeQuery(query).setParameter("value", value).getSingleResult())
				.longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<T> findByField(String fieldName, Object value, Class<T> entityType) {
		String query = "SELECT * FROM " + getTableName(entityType) + " WHERE " + fieldName + " = :value";
		try {
			T result = (T) entityManager.createNativeQuery(query, entityType).setParameter("value", value)
					.getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Class<T> entityType) {
		String query = "SELECT * FROM " + getTableName(entityType);
		return entityManager.createNativeQuery(query, entityType).getResultList();
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

	@Override
	@Transactional
	public List<T> saveAll(List<T> entities) {
		for (T entity : entities) {
			entityManager.persist(entity);
		}
		return entities;
	}

}