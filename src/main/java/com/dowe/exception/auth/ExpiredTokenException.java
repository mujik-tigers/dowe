package com.dowe.exception.auth;

import com.dowe.exception.ErrorType;

public class ExpiredTokenException extends TokenException {

	public ExpiredTokenException(TokenType type) {
		super(ErrorType.EXPIRED_TOKEN, type);
	}

}
