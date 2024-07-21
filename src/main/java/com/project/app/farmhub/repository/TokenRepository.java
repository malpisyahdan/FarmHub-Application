package com.project.app.farmhub.repository;

import java.util.List;
import java.util.Optional;

import com.project.app.farmhub.entity.Token;

public interface TokenRepository extends MasterRepository<Token, String> {

	List<Token> findAllAccessTokensByUser(String userId);

	Optional<Token> findByAccessToken(String token);

}
