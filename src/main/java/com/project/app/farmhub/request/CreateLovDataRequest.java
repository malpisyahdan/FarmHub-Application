package com.project.app.farmhub.request;

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
	private Integer ordNumber;

	@NotNull(message = "is editable cannot be empty.")
	private Boolean isActive;
}
