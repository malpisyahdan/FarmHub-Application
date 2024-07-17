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
public class CreatePaymentRequest {

	@NotBlank(message = "order id cannot be empty.")
	private String orderId;

	@NotBlank(message = "payment method cannot be empty.")
	private String lovPaymentMethodId;

}
