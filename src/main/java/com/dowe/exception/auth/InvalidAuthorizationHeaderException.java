package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class InvalidAuthorizationHeaderException extends CustomException {

	public InvalidAuthorizationHeaderException() {
		super(ErrorType.INVALID_AUTHORIZATION_HEADER);
	}

}
