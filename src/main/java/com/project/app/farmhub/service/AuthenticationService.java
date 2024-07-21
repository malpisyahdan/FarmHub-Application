package com.project.app.farmhub.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.farmhub.entity.Token;
import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.repository.MasterRepository;
import com.project.app.farmhub.repository.TokenRepository;
import com.project.app.farmhub.response.AuthenticationResponse;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

	private final MasterRepository<User, String> repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	private final TokenRepository tokenRepository;

	private final AuthenticationManager authenticationManager;

	@Transactional
	public AuthenticationResponse register(User request) {
		if (repository.findByField("username", request.getUsername(), User.class).isPresent()) {
			return new AuthenticationResponse(null, "User already exists");
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		user = repository.save(user);

		String accessToken = jwtService.generateAccessToken(user);

		saveUserToken(accessToken, user);

		return new AuthenticationResponse(accessToken, "User registration was successful");
	}

	@Transactional
	public AuthenticationResponse authenticate(User request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = repository.findByField("username", request.getUsername(), User.class).orElseThrow();
		String accessToken = jwtService.generateAccessToken(user);

		revokeAllTokenByUser(user);
		saveUserToken(accessToken, user);

		return new AuthenticationResponse(accessToken, "User login was successful");

	}

	private void revokeAllTokenByUser(User user) {
		List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
		if (validTokens.isEmpty()) {
			return;
		}

		validTokens.forEach(t -> {
			t.setLoggedOut(true);
		});

		tokenRepository.saveAll(validTokens);
	}

	private void saveUserToken(String accessToken, User user) {
		Token token = new Token();
		token.setAccessToken(accessToken);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

}