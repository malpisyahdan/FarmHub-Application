package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.LovData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class LovDataRepositoryImpl extends MasterRepositoryImpl<LovData, String> implements LovDataRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean existsByLovTypeAndCode(String lovType, String code) {
		String query = "SELECT COUNT(*) FROM " + getTableName(LovData.class)
				+ " WHERE lov_type = :lovType AND code = :code";
		long count = ((Number) entityManager.createNativeQuery(query).setParameter("lovType", lovType)
				.setParameter("code", code).getSingleResult()).longValue();
		return count > 0;
	}

	@Override
	public Optional<LovData> findByLovTypeAndCode(String lovType, String code) {
		String query = "SELECT * FROM " + getTableName(LovData.class) + " WHERE lov_type = :lovType AND code = :code";
		try {
			LovData result = (LovData) entityManager.createNativeQuery(query, LovData.class)
					.setParameter("lovType", lovType).setParameter("code", code).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LovData> findByLovTypeAndIsActive(String lovType, Boolean isActive) {
		String query = "SELECT * FROM " + getTableName(LovData.class)
				+ " WHERE lov_type = :lovType AND isActive = :isActive";
		return entityManager.createNativeQuery(query, LovData.class).setParameter("lovType", lovType)
				.setParameter("isActive", isActive).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LovData> findByLovType(String lovType) {
		String query = "SELECT * FROM " + getTableName(LovData.class) + " WHERE lov_type = :lovType";
		return entityManager.createNativeQuery(query, LovData.class).setParameter("lovType", lovType).getResultList();
	}
}