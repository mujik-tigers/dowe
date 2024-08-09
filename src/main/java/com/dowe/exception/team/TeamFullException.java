package com.dowe.exception.team;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class TeamFullException extends CustomException {

	public TeamFullException() {
		super(ErrorType.TEAM_IS_FULL);
	}

}
