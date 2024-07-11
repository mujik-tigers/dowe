package com.dowe.auth;

import lombok.Getter;

@Getter
public enum TokenType {

	ACCESS("access token"), REFRESH("refresh token"), FAKE("fake token");

	private final String description;

	TokenType(String description) {
		this.description = description;
	}

}
