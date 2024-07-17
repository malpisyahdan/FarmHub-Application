package com.project.app.farmhub.helper;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.project.app.farmhub.response.WebResponse;

public class ResponseHelper {

	public static <T> WebResponse<T> ok(T data) {
		return ResponseHelper.status(HttpStatus.OK, data, null, null);
	}

	public static <T> WebResponse<T> status(HttpStatus status, T data, Map<String, List<String>> errors,
			Map<String, Object> metadata) {
		return WebResponse.<T>builder().code(status.value()).status(status.name()).data(data).build();
	}

}