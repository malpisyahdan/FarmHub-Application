package com.project.app.farmhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_lov_data", uniqueConstraints = { @UniqueConstraint(columnNames = { "lov_type", "code" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LovData extends MasterEntity {

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "lov_type")
	private String lovType;

	@Column(name = "ord_number")
	private Integer ordNumber;

	@Column(name = "is_active")
	private Boolean isActive;

}
