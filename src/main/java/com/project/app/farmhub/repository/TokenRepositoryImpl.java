package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.project.app.farmhub.entity.Token;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class TokenRepositoryImpl extends MasterRepositoryImpl<Token, String> implements TokenRepository{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Token> findAllAccessTokensByUser(String userId) {
        return entityManager.createQuery("""
                select t from Token t inner join User u on t.user.id = u.id
                where t.user.id = :userId and t.loggedOut = false
                """)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Token> findByAccessToken(String token) {
        List<Token> tokens = entityManager.createQuery("select t from Token t where t.accessToken = :token", Token.class)
                .setParameter("token", token)
                .getResultList();
        if (tokens.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(tokens.get(0));
        }
    }

}
