package com.project.app.farmhub.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_product", uniqueConstraints = { @UniqueConstraint(columnNames = { "code" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends MasterEntity {

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "stock")
	private Integer stock;

	@ManyToOne
	@JoinColumn(name = "farmer_id")
	private User farmer;

}
