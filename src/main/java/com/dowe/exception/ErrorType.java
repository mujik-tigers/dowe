package com.dowe.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	// Auth
	INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다"),
	EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다");

	private final HttpStatus status;
	private final String message;

	ErrorType(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
