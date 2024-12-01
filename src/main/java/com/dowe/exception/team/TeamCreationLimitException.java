package com.dowe.exception.team;

import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;

public class TeamCreationLimitException extends CustomException {

  public TeamCreationLimitException() {
    super(ErrorType.TEAM_CREATION_LIMIT_EXCEPTION);
  }
}
