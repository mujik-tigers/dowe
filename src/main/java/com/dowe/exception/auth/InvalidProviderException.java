package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class InvalidProviderException extends CustomException {

	public InvalidProviderException() {
		super(ErrorType.INVALID_PROVIDER);
	}

}
