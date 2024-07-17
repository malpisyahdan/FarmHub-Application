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
@Table(name = "t_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends MasterEntity {

	@ManyToOne
	@JoinColumn(name = "umkm_id")
	private User umkm;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "comment")
	private String comment;

}
