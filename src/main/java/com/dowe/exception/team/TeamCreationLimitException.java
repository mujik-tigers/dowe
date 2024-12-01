package com.dowe.exception.team;

public class TeamCreationLimitException extends RuntimeException {

  public TeamCreationLimitException(String message) {
    super(message);
  }
}
