package com.dowe.exception.auth;

import com.dowe.auth.TokenType;
import com.dowe.exception.ErrorType;

public class ExpiredTokenException extends TokenException {

	public ExpiredTokenException(TokenType currentTokenType, TokenType needTokenType) {
		super(ErrorType.EXPIRED_TOKEN, currentTokenType, needTokenType);
	}

}
