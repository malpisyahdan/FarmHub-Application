package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Shipment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class ShipmentRepository implements MasterRepository<Shipment>{

	@PersistenceContext
	private EntityManager entityManager;
	
	public Optional<Shipment> findByField(Object value, Class<Shipment> entityType) {
		String sql = "SELECT * FROM t_shipment WHERE order_id = :value";
		Query query = entityManager.createNativeQuery(sql, entityType);
		query.setParameter("value", value);
		List<?> results = query.getResultList();
		List<Shipment> shipment = results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
		return shipment.stream().findFirst();
	}

	@Override
	public List<Shipment> findAll(Class<Shipment> entityType) {
		String sql = "SELECT * FROM t_shipment";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<Shipment> findById(String id, Class<Shipment> entityType) {
		Shipment entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public Shipment save(Shipment entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Shipment entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}
		
	}

}
