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
public class ShipmentResponse {

	private String id;
	private String orderId;

	private String trackingNumber;
	private String courierService;
	private String statusShipment;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
