package com.project.app.farmhub.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.app.farmhub.entity.User;

public class SecurityHelper {

	public static String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User user = (User) authentication.getPrincipal();
			return user.getId();
		}
		return null;
	}

	public static boolean hasRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
			return true;
		}
		return false;
	}

}
