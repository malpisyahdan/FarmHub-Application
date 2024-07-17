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
public class LovDataResponse {

	private String id;
	private String code;
	private String name;
	private String lovType;
	private Integer ordNumber;
	private Boolean isActive;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private Long version;

}
