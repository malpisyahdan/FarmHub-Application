package com.project.app.farmhub.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private String id;
	private String farmerId;
	private String farmerName;

	private String umkmId;
	private String umkmName;

	private String productId;
	private String productName;
	private BigDecimal productPrice;

	private BigDecimal totalPrice;
	private Integer quantity;

	private String status;
	private String statusPayment;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
