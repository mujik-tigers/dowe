package com.dowe.exception.team;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class AlreadyInTeamException extends CustomException {

	public AlreadyInTeamException() {
		super(ErrorType.MEMBER_IS_ALREADY_IN_TEAM);
	}

}
