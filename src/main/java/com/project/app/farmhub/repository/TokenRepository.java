package com.project.app.farmhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.app.farmhub.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query("""
			select t from Token t inner join User u on t.user.id = u.id
			where t.user.id = :userId and t.loggedOut = false
			""")
	List<Token> findAllAccessTokensByUser(String userId);

	Optional<Token> findByAccessToken(String token);

}