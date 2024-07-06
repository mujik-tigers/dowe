package com.dowe.exception.auth;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class ExpiredTokenException extends CustomException {

	public ExpiredTokenException() {
		super(ErrorType.EXPIRED_TOKEN);
	}

}
