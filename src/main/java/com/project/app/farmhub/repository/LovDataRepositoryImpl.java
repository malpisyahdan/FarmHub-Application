package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.LovData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class LovDataRepositoryImpl extends MasterRepositoryImpl<LovData, String> implements LovDataRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean existsByLovTypeAndCode(String lovType, String code) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<LovData> root = criteriaQuery.from(LovData.class);

		Predicate lovTypePredicate = criteriaBuilder.equal(root.get("lovType"), lovType);
		Predicate codePredicate = criteriaBuilder.equal(root.get("code"), code);
		criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.and(lovTypePredicate, codePredicate));

		long count = entityManager.createQuery(criteriaQuery).getSingleResult();
		return count > 0;
	}

	@Override
	public Optional<LovData> findByLovTypeAndCode(String lovType, String code) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<LovData> criteriaQuery = criteriaBuilder.createQuery(LovData.class);

		Root<LovData> root = criteriaQuery.from(LovData.class);

		Predicate lovTypePredicate = criteriaBuilder.equal(root.get("lovType"), lovType);
		Predicate codePredicate = criteriaBuilder.equal(root.get("code"), code);
		criteriaQuery.select(root).where(criteriaBuilder.and(lovTypePredicate, codePredicate));

		try {
			LovData result = entityManager.createQuery(criteriaQuery).getSingleResult();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<LovData> findByLovTypeAndIsActive(String lovType, Boolean isActive) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<LovData> criteriaQuery = criteriaBuilder.createQuery(LovData.class);

		Root<LovData> root = criteriaQuery.from(LovData.class);

		Predicate lovTypePredicate = criteriaBuilder.equal(root.get("lovType"), lovType);
		Predicate isActivePredicate = criteriaBuilder.equal(root.get("isActive"), isActive);
		criteriaQuery.select(root).where(criteriaBuilder.and(lovTypePredicate, isActivePredicate));

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<LovData> findByLovType(String lovType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<LovData> criteriaQuery = criteriaBuilder.createQuery(LovData.class);

		Root<LovData> root = criteriaQuery.from(LovData.class);

		Predicate lovTypePredicate = criteriaBuilder.equal(root.get("lovType"), lovType);
		criteriaQuery.select(root).where(lovTypePredicate);

		return entityManager.createQuery(criteriaQuery).getResultList();
	}
}