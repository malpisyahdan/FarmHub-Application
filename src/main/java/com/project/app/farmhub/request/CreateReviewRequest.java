package com.project.app.farmhub.request;

import jakarta.validation.constraints.NotBlank;
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

	@NotBlank(message = "rating cannot be empty.")
	private Integer rating;

	@NotBlank(message = "comment cannot be empty.")
	private String comment;

}
