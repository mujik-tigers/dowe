package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class InvalidTokenException extends CustomException {

	public InvalidTokenException(ErrorType errorType) {
		super(errorType);
	}

}
