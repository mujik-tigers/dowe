package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

import lombok.Getter;

@Getter
public class TokenException extends CustomException {

	private final TokenType needTokenType;

	public TokenException(ErrorType errorType, TokenType type) {
		super(errorType);
		this.needTokenType = type;
	}

}
