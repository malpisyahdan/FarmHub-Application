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
public class CreateShipmentRequest {
	
	@NotBlank(message = "order id cannot be empty.")
	private String orderId;

	@NotBlank(message = "courier service cannot be empty.")
	private String lovCourierServiceId;

}
