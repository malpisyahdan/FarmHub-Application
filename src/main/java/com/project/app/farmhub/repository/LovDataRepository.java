package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.LovData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class LovDataRepository implements MasterRepository<LovData> {

	@PersistenceContext
	private EntityManager entityManager;

	public boolean existsByLovTypeAndCode(String lovType, String code) {
		String sql = "SELECT COUNT(*) > 0 FROM t_lov_data WHERE lov_type = :lovType AND code = :code";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("lovType", lovType);
		query.setParameter("code", code);

		Object result = query.getSingleResult();
		return (Boolean) result;
	}

	public Optional<LovData> findByLovTypeAndCode(String lovType, String code) {
		String sql = "SELECT * FROM t_lov_data WHERE lov_type = :lovType AND code = :code";
		Query query = entityManager.createNativeQuery(sql, LovData.class);
		query.setParameter("lovType", lovType);
		query.setParameter("code", code);

		try {
			LovData result = (LovData) query.getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("unchecked")
	public List<LovData> findByLovTypeAndIsActive(String lovType, Boolean isActive) {
		String sql = "SELECT * FROM t_lov_data WHERE lov_type = :lovType AND is_active = :isActive";
		Query query = entityManager.createNativeQuery(sql, LovData.class);
		query.setParameter("lovType", lovType);
		query.setParameter("isActive", isActive);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<LovData> findByLovType(String lovType) {
		String sql = "SELECT * FROM t_lov_data WHERE lov_type = :lovType";
		Query query = entityManager.createNativeQuery(sql, LovData.class);
		query.setParameter("lovType", lovType);

		return query.getResultList();
	}

	@Override
	public List<LovData> findAll(Class<LovData> entityType) {
		String sql = "SELECT * FROM t_lov_data";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<LovData> findById(String id, Class<LovData> entityType) {		
		LovData entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public LovData save(LovData entity) {
		entityManager.persist(entity);
		entityManager.flush();
		return entity;
	}

	@Override
	@Transactional
	public void delete(LovData entity) {
		entityManager.remove(entity);
		entityManager.flush();
		
	}

}