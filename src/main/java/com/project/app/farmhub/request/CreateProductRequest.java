package com.project.app.farmhub.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

	@NotBlank(message = "code cannot be empty.")
	private String code;

	@NotBlank(message = "name cannot be empty.")
	private String name;

	private String description;

	@NotBlank(message = "price cannot be empty.")
	private BigDecimal price;

	@NotBlank(message = "stock cannot be empty.")
	private Integer stock;

}
