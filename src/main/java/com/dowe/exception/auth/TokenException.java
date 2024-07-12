package com.dowe.exception.auth;

import com.dowe.auth.TokenType;
import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

import lombok.Getter;

@Getter
public class TokenException extends CustomException {

	private final TokenType currentTokenType;
	private final TokenType needTokenType;

	public TokenException(ErrorType errorType, TokenType currentTokenType, TokenType needTokenType) {
		super(errorType);
		this.currentTokenType = currentTokenType;
		this.needTokenType = needTokenType;
	}

}
