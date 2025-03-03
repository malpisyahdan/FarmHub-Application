package com.project.app.farmhub.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

	private final UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByField(username, User.class)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public Optional<User> getEntityById(String id) {
		return repository.findById(id, User.class);
	}
}