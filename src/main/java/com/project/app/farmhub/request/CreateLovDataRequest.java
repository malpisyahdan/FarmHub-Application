package com.project.app.farmhub.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLovDataRequest {

	@NotBlank(message = "code cannot be empty.")
	private String code;

	@NotBlank(message = "name cannot be empty.")
	private String name;

	@NotBlank(message = "lov type cannot be empty.")
	private String lovType;

	@NotNull(message = "order number cannot be empty.")
	@Min(value = 0, message = "order number must be a positive value.")
	private Integer ordNumber;

	@NotNull(message = "is active cannot be empty.")
	private Boolean isActive;
}
