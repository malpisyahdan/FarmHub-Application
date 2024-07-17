package com.project.app.farmhub.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

	private String id;
	private String orderId;

	private String paymentMethod;
	private String statusPayment;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
