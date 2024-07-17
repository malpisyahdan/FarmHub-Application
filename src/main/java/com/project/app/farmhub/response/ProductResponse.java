package com.project.app.farmhub.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	private String id;
	private String farmerId;
	private String farmerName;
	private String code;
	private String name;
	private String description;

	private BigDecimal price;
	private Integer stock;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
