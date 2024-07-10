package com.dowe.util.api;

import lombok.Getter;

@Getter
public enum ResponseResult {

	// Auth
	LOGIN_SUCCESS("로그인을 성공했습니다"),
	REFRESH_SUCCESS("토큰 리프레싱을 성공했습니다"),

	// Common
	EXCEPTION_OCCURRED("예외가 발생했습니다");

	private final String description;

	ResponseResult(String description) {
		this.description = description;
	}

}
