package com.dowe.exception.auth;

import com.dowe.exception.ErrorType;

public class InvalidTokenException extends TokenException {

	public InvalidTokenException(TokenType type) {
		super(ErrorType.INVALID_TOKEN, type);
	}

}
