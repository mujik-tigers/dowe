package com.dowe.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로그인 도중 문제가 발생했습니다 다시 시도해 주세요");

	private final HttpStatus status;
	private final String message;

	ErrorType(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
