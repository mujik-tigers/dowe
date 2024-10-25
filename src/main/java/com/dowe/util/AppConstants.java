package com.dowe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {

  public static final String MEMBER_ID = "memberId";
  public static final String AUTHORIZATION_CODE = "authorization_code";
  public static final String BEARER = "Bearer ";
  public static final String X_WWW_FORM_URLENCODED_CHARSET_UTF_8 = "application/x-www-form-urlencoded;charset=utf-8";
  public static final String MEMBER_CODE_SET = "memberCode";
  public static final String TOKEN_TYPE = "tokenType";
  public static final String TEAM_TITLE = "title";
  public static final String UPDATED_AT = "updated_at";
  public static final String TIE_BREAKER_ID = "tie_breaker_id";
  public static final String FRONTEND_ORIGIN = "https://dowith.today";
  public static final String API_HOST = "api.dowith.today";

  public static final int TEAM_MAX_SIZE = 10;
  public static final int LAST_UNIX_TIMESTAMP_INDEX = 0;
  public static final int LAST_TIE_BREAKER_ID_INDEX = 1;
  public static final int NO_MORE_LAST_HIT_OFFSET = 1;
  public static final int HAS_MORE_LAST_HIT_OFFSET = 2;

}
