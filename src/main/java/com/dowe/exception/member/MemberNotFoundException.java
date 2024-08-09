package com.dowe.exception.member;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class MemberNotFoundException extends CustomException {

	public MemberNotFoundException() {
		super(ErrorType.MEMBER_NOT_FOUND);
	}

}
