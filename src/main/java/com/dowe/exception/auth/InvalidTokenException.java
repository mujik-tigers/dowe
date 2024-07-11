package com.dowe.exception.auth;

import com.dowe.auth.TokenType;
import com.dowe.exception.ErrorType;

public class InvalidTokenException extends TokenException {

	public InvalidTokenException(TokenType currentTokenType, TokenType needTokenType) {
		super(ErrorType.INVALID_TOKEN, currentTokenType, needTokenType);
	}

}
