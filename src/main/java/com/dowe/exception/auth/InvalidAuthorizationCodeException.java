package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class InvalidAuthorizationCodeException extends CustomException {

	public InvalidAuthorizationCodeException() {
		super(ErrorType.INVALID_AUTHORIZATION_CODE);
	}

}
