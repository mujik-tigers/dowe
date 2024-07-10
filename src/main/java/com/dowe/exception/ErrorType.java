package com.dowe.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	// Auth
	INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth Provider입니다"),
	INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다"),
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 헤더입니다"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
	EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다");

	private final HttpStatus status;
	private final String message;

	ErrorType(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
