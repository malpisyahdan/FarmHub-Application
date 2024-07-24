package com.project.app.farmhub.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

	@NotNull(message = "price cannot be null.")
	@Min(value = 0, message = "price must be a positive value.")
	private BigDecimal price;

	@NotNull(message = "stock cannot be null.")
	@Min(value = 0, message = "stock must be a positive value.")
	private Integer stock;

}
