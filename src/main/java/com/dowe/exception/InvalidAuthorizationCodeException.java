package com.dowe.exception;

public class InvalidAuthorizationCodeException extends CustomException {

	public InvalidAuthorizationCodeException() {
		super(ErrorType.INVALID_AUTHORIZATION_CODE);
	}

}
