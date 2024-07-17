package com.project.app.farmhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends MasterEntity{

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "is_logged_out")
	private boolean loggedOut;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
