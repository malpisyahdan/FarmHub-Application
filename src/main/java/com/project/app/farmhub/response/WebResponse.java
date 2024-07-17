package com.project.app.farmhub.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard Web Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

	/**
	 * Code , usually same as HTTP Code
	 */
	@JsonProperty("code")
	private Integer code;

	/**
	 * Status, usually same as HTTP status
	 */
	@JsonProperty("status")
	private String status;

	/**
	 * Response data
	 */
	@JsonProperty("data")
	private T data;

}
