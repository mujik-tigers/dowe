package com.dowe.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	// Auth
	INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth Provider입니다"),
	INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다"),
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 헤더입니다"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "멤버가 존재하지 않습니다"),

	// Team
	TEAM_CREATION_LIMIT_EXCEPTION(HttpStatus.CONFLICT, "사용자가 이미 5개의 팀에 참여 중이라 새로운 팀을 생성할 수 없습니다."),
	MEMBER_IS_ALREADY_IN_TEAM(HttpStatus.BAD_REQUEST, "이미 소속된 팀입니다"),
	TEAM_IS_FULL(HttpStatus.BAD_REQUEST, "팀이 가득 차 참여할 수 없습니다");

	private final HttpStatus status;
	private final String message;

	ErrorType(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
