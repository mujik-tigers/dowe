package com.dowe.exception;

public class ExpiredTokenException extends CustomException {

	public ExpiredTokenException() {
		super(ErrorType.EXPIRED_TOKEN);
	}

}
