package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Token;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class TokenRepository implements MasterRepository<Token> {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")

	@Transactional
	public List<Token> findAllAccessTokensByUser(String userId) {
		return entityManager.createNativeQuery("""
				select t.* from t_token t
				inner join t_user u on t.user_id = u.id
				where t.user_id = :userId and t.logged_out = false
				""", Token.class).setParameter("userId", userId).getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Optional<Token> findByAccessToken(String token) {
		List<Token> tokens = entityManager.createNativeQuery("""
				select * from t_token where access_token = :token
				""", Token.class).setParameter("token", token).getResultList();

		if (tokens.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(tokens.get(0));
		}
	}

	@Override
	public List<Token> findAll(Class<Token> entityType) {
		String sql = "SELECT * FROM t_token";
		Query query = entityManager.createNativeQuery(sql, entityType);
		List<?> results = query.getResultList();
		return results.stream().filter(entityType::isInstance).map(entityType::cast).toList();
	}

	@Override
	public Optional<Token> findById(String id, Class<Token> entityType) {
		Token entity = entityManager.find(entityType, id);
		return Optional.ofNullable(entity);
	}

	@Override
	@Transactional
	public Token save(Token entity) {
		if (entity.getId() == null) {
			entityManager.persist(entity);
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	@Override
	@Transactional
	public void delete(Token entity) {
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}

	}

}
