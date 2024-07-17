package com.project.app.farmhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.farmhub.entity.User;
import com.project.app.farmhub.response.AuthenticationResponse;
import com.project.app.farmhub.service.AuthenticationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping({ "/api/v1/farmhub" })
public class AuthenticationController {

	private final AuthenticationService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody User request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

}