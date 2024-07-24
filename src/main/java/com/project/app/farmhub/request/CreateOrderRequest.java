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
public class CreateOrderRequest {

	@NotBlank(message = "product id cannot be empty.")
	private String productId;

	@NotNull(message = "quantity cannot be null.")
	@Min(value = 0, message = "quantity must be a positive value.")
	private Integer quantity;

}
