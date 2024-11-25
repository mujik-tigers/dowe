package com.dowe.util.api;

import lombok.Getter;

@Getter
public enum ResponseResult {

  // Auth
  LOGIN_SUCCESS("로그인을 성공했습니다"),
  TOKEN_REFRESH_SUCCESS("토큰 업데이트에 성공했습니다"),

  // Common
  EXCEPTION_OCCURRED("예외가 발생했습니다"),

  // Member
  MEMBER_NAME_UPDATE_SUCCESS("이름을 성공적으로 변경하였습니다"),
  MY_TEAM_LIST_FETCH_SUCCESS("나의 팀 목록 조회에 성공했습니다"),

  // Team
  TEAM_CREATE_SUCCESS("팀 생성에 성공했습니다"),

  // Random
  RANDOM_TEAMS_SUCCESS("랜덤 팀 조회에 성공했습니다."),

  // Participation
  MY_PENDING_REQUESTS_SUCCESS("가입 승인 대기 중인 요청 조회에 성공했습니다."),
  DELETE_PENDING_REQUEST_SUCCESS("가입 승인 대기 중인 요청 삭제에 성공했습니다."),

  // Search
  TEAM_SEARCH_SUCCESS("팀 검색에 성공했습니다.");

  private final String description;

  ResponseResult(String description) {
    this.description = description;
  }

}
