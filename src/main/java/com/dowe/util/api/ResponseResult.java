package com.dowe.util.api;

import lombok.Getter;

@Getter
public enum ResponseResult {

	// Common
	EXCEPTION_OCCURRED("예외가 발생했습니다");

	private final String description;

	ResponseResult(String description) {
		this.description = description;
	}

}
