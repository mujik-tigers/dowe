package com.dowe.auth.presentation;

import static com.dowe.TestConstants.AUTHORIZATION;
import static com.dowe.TestConstants.BACKEND_DOMAIN;
import static com.dowe.TestConstants.BEARER;
import static com.dowe.TestConstants.FRONTEND_DOMAIN;
import static com.dowe.TestConstants.HOST;
import static com.dowe.TestConstants.ORIGIN;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import com.dowe.RestDocsSupport;
import com.dowe.auth.TokenType;
import com.dowe.auth.application.AuthService;
import com.dowe.auth.dto.LoginData;
import com.dowe.auth.dto.TokenPair;
import com.dowe.exception.ErrorType;
import com.dowe.exception.auth.ExpiredTokenException;
import com.dowe.exception.auth.InvalidAuthorizationCodeException;
import com.dowe.exception.auth.InvalidAuthorizationHeaderException;
import com.dowe.exception.auth.InvalidProviderException;
import com.dowe.exception.auth.InvalidTokenException;
import com.dowe.member.Provider;
import com.dowe.util.api.ResponseResult;

class AuthControllerTest extends RestDocsSupport {

  @Autowired
  protected AuthService authService;

  @Test
  @DisplayName("회원가입 성공")
  void signUpSuccess() throws Exception {
    // given
    String authorizationCode = "auth-code";
    Provider provider = Provider.GOOGLE;

    LoginData data = LoginData.builder()
        .code("GDE1C")
        .name("몽블랑130")
        .accessToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4Nzk5NiwibWVtYmVySWQiOjF9.20VhnUqeJASQuoeCCpi8Mlq6RFqKja98rXWJxlfX3QE")
        .refreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4OTIwNCwibWVtYmVySWQiOjF9.EBl_ghoXcLZ7o6uQ-OscEolQoFm8deglLeDz1bZ60l8")
        .firstTime(true)
        .build();

    given(authService.login(FRONTEND_DOMAIN, provider, authorizationCode))
        .willReturn(data);

    // when // then
    mockMvc.perform(post("/oauth/{provider}", provider.name().toLowerCase())
            .param("authorizationCode", authorizationCode)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.LOGIN_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.code").value(data.getCode()))
        .andExpect(jsonPath("$.data.name").value(data.getName()))
        .andExpect(jsonPath("$.data.accessToken").value(data.getAccessToken()))
        .andExpect(jsonPath("$.data.refreshToken").value(data.getRefreshToken()))
        .andExpect(jsonPath("$.data.firstTime").value(data.isFirstTime()))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("provider").description("OAuth Provider")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.code").type(JsonFieldType.STRING).description("유저 식별 코드"),
                fieldWithPath("data.name").type(JsonFieldType.STRING).description("유저 이름"),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                    .description("Access Token"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                    .description("Refresh Token"),
                fieldWithPath("data.firstTime").type(JsonFieldType.BOOLEAN).description("첫 회원인지 여부")
            )
        ));
  }

  @Test
  @DisplayName("로그인 성공")
  void loginSuccess() throws Exception {
    // given
    String authorizationCode = "auth-code";
    Provider provider = Provider.GOOGLE;

    LoginData data = LoginData.builder()
        .code("GDE1C")
        .name("몽블랑130")
        .accessToken(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4Nzk5NiwibWVtYmVySWQiOjF9.20VhnUqeJASQuoeCCpi8Mlq6RFqKja98rXWJxlfX3QE")
        .refreshToken(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4OTIwNCwibWVtYmVySWQiOjF9.EBl_ghoXcLZ7o6uQ-OscEolQoFm8deglLeDz1bZ60l8")
        .firstTime(false)
        .build();

    given(authService.login(FRONTEND_DOMAIN, provider, authorizationCode))
        .willReturn(data);

    // when // then
    mockMvc.perform(post("/oauth/{provider}", provider.name().toLowerCase())
            .param("authorizationCode", authorizationCode)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.LOGIN_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.code").value(data.getCode()))
        .andExpect(jsonPath("$.data.name").value(data.getName()))
        .andExpect(jsonPath("$.data.accessToken").value(data.getAccessToken()))
        .andExpect(jsonPath("$.data.refreshToken").value(data.getRefreshToken()))
        .andExpect(jsonPath("$.data.firstTime").value(data.isFirstTime()))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("provider").description("OAuth Provider")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.code").type(JsonFieldType.STRING).description("유저 식별 코드"),
                fieldWithPath("data.name").type(JsonFieldType.STRING).description("유저 이름"),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                    .description("Access Token"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                    .description("Refresh Token"),
                fieldWithPath("data.firstTime").type(JsonFieldType.BOOLEAN).description("첫 회원인지 여부")
            )
        ));
  }

  @Test
  @DisplayName("로그인 실패 : 지원하지 않는 OAuth Provider")
  void invalidProvider() throws Exception {
    // given
    String authorizationCode = "auth-code";

    // when // then
    mockMvc.perform(post("/oauth/{provider}", "goooogle")
            .param("authorizationCode", authorizationCode)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value(InvalidProviderException.class.getSimpleName()))
        .andExpect(jsonPath("$.data[0].message").value(ErrorType.INVALID_PROVIDER.getMessage()))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("provider").description("OAuth Provider")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지")
            )
        ));
  }

  @Test
  @DisplayName("로그인 실패 : 유효하지 않은 인증 코드")
  void invalidAuthorizationCode() throws Exception {
    // given
    String authorizationCode = "auth-code";
    Provider provider = Provider.GOOGLE;

    given(authService.login(FRONTEND_DOMAIN, provider, authorizationCode))
        .willThrow(new InvalidAuthorizationCodeException());

    // when // then
    mockMvc.perform(post("/oauth/{provider}", provider.name().toLowerCase())
            .param("authorizationCode", authorizationCode)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value(
            InvalidAuthorizationCodeException.class.getSimpleName()))
        .andExpect(
            jsonPath("$.data[0].message").value(ErrorType.INVALID_AUTHORIZATION_CODE.getMessage()))
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("provider").description("OAuth Provider")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지")
            )
        ));
  }

  @Test
  @DisplayName("토큰 리프레싱 실패 : 유효하지 않은 인증 헤더")
  void refreshFail_invalidAuthorizationHeader() throws Exception {
    // when / then
    mockMvc.perform(patch("/refresh")
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value(
            InvalidAuthorizationHeaderException.class.getSimpleName()))
        .andExpect(jsonPath("$.data[0].message").value(
            ErrorType.INVALID_AUTHORIZATION_HEADER.getMessage()))
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지")
            )
        ));
  }

  @Test
  @DisplayName("토큰 refresh 성공")
  void refreshToken() throws Exception {
    // given
    String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjA3NTc5NTksImV4cCI6MTcyMTk2NzU1OSwibWVtYmVySWQiOjQsInRva2VuVHlwZSI6IlJFRlJFU0gifQ.O4YHeol1a2yADRCd0zBtpJyThJxeOVjKor_cRLPUTZc";
    String newRefreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjA3NTc5ODcsImV4cCI6MTcyMTk2NzU4NywibWVtYmVySWQiOjQsInRva2VuVHlwZSI6IlJFRlJFU0gifQ.kIIL6JWkBPARjnheKouAuv0kuOfIIATxEHiuhtsq2w";
    String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjA3NTc5ODcsImV4cCI6MTcyMDc1OTc4NywibWVtYmVySWQiOjQsInRva2VuVHlwZSI6IkFDQ0VTUyJ9.7qE6ExR0FAN1aYfekwZ7Lb1eP4BOTv-MNRCFpc9hbhs";

    doReturn(true).when(authorizationHeaderInterceptor)
        .preHandle(any(), any(), any());

    given(authService.refresh(eq(refreshToken)))
        .willReturn(new TokenPair(accessToken, newRefreshToken));

    // when // then
    mockMvc.perform(patch("/refresh")
            .header(AUTHORIZATION, BEARER + refreshToken)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(
            jsonPath("$.result").value(ResponseResult.TOKEN_REFRESH_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.accessToken").value(accessToken))
        .andExpect(jsonPath("$.data.refreshToken").value(newRefreshToken))
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName(AUTHORIZATION).description("refresh token")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                    .description("access token"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                    .description("refresh token")
            )
        ));
  }

  @Test
  @DisplayName("토큰 refresh 실패 : refreshToken이 아닌 accessToken을 전달")
  void refreshTokenFailInvalidTokenType() throws Exception {
    // given
    String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjA3NTc5ODcsImV4cCI6MTcyMDc1OTc4NywibWVtYmVySWQiOjQsInRva2VuVHlwZSI6IkFDQ0VTUyJ9.7qE6ExR0FAN1aYfekwZ7Lb1eP4BOTv-MNRCFpc9hbhs";

    doReturn(true).when(authorizationHeaderInterceptor)
        .preHandle(any(), any(), any());

    given(authService.refresh(eq(accessToken)))
        .willThrow(new InvalidTokenException(TokenType.ACCESS, TokenType.REFRESH));

    // when // then
    mockMvc.perform(patch("/refresh")
            .header(AUTHORIZATION, BEARER + accessToken)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value(InvalidTokenException.class.getSimpleName()))
        .andExpect(jsonPath("$.data[0].message").value(ErrorType.INVALID_TOKEN.getMessage()))
        .andExpect(jsonPath("$.data[0].currentTokenType").value(TokenType.ACCESS.getDescription()))
        .andExpect(jsonPath("$.data[0].needTokenType").value(TokenType.REFRESH.getDescription()))
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName(AUTHORIZATION).description("access token")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지"),
                fieldWithPath("data[].currentTokenType").type(JsonFieldType.STRING)
                    .description("클라이언트가 보낸 토큰 타입"),
                fieldWithPath("data[].needTokenType").type(JsonFieldType.STRING)
                    .description("실제 필요한 토큰 타입")
            )
        ));
  }

  @Test
  @DisplayName("토큰 리프레싱 실패 : 토큰 유효기간 만료")
  void refreshTokenFailExpiredToken() throws Exception {
    // given
    String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjA3NTc5ODcsImV4cCI6MTcyMDc1OTc4NywibWVtYmVySWQiOjQsInRva2VuVHlwZSI6IkFDQ0VTUyJ9.7qE6ExR0FAN1aYfekwZ7Lb1eP4BOTv-MNRCFpc9hbhs";

    given(authService.refresh(eq(refreshToken)))
        .willThrow(new ExpiredTokenException(TokenType.REFRESH, TokenType.REFRESH));

    // when // then
    mockMvc.perform(patch("/refresh")
            .header(AUTHORIZATION, BEARER + refreshToken)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value(ExpiredTokenException.class.getSimpleName()))
        .andExpect(jsonPath("$.data[0].message").value(ErrorType.EXPIRED_TOKEN.getMessage()))
        .andExpect(jsonPath("$.data[0].currentTokenType").value(TokenType.REFRESH.getDescription()))
        .andExpect(jsonPath("$.data[0].needTokenType").value(TokenType.REFRESH.getDescription()))
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName(AUTHORIZATION).description("refresh token")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지"),
                fieldWithPath("data[].currentTokenType").type(JsonFieldType.STRING)
                    .description("클라이언트가 보낸 토큰 타입"),
                fieldWithPath("data[].needTokenType").type(JsonFieldType.STRING)
                    .description("실제 필요한 토큰 타입")
            )
        ));
  }

}
