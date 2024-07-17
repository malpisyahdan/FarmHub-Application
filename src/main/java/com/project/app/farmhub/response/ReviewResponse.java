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
public class ReviewResponse {

	private String id;
	private String umkmId;
	private String umkmName;
	private String productId;
	private String productCode;
	private String productName;
	private Integer rating;
	private String comment;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
