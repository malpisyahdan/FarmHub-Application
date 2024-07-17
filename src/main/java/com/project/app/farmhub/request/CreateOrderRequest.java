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
public class CreateOrderRequest {

	@NotBlank(message = "product id cannot be empty.")
	private String productId;

	@NotBlank(message = "quantity cannot be empty.")
	private Integer quantity;

}
