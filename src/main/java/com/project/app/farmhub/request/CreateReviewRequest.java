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
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

	@NotBlank(message = "product id cannot be empty.")
	private String productId;

	@NotNull(message = "rating cannot be null.")
	@Min(value = 0, message = "rating must be a positive value.")
	private Integer rating;

	@NotBlank(message = "comment cannot be empty.")
	private String comment;

}
