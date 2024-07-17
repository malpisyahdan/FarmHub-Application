package com.project.app.farmhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.app.farmhub.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
}