package com.dowe.auth.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
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
import com.dowe.auth.application.AuthService;
import com.dowe.auth.dto.LoginData;
import com.dowe.exception.ErrorType;
import com.dowe.exception.auth.InvalidAuthorizationCodeException;
import com.dowe.exception.auth.InvalidProviderException;
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
			.accessToken(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4Nzk5NiwibWVtYmVySWQiOjF9.20VhnUqeJASQuoeCCpi8Mlq6RFqKja98rXWJxlfX3QE")
			.refreshToken(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkb3dpdGgiLCJpYXQiOjE3MjAxODc5OTQsImV4cCI6MTcyMDE4OTIwNCwibWVtYmVySWQiOjF9.EBl_ghoXcLZ7o6uQ-OscEolQoFm8deglLeDz1bZ60l8")
			.firstTime(true)
			.build();

		given(authService.login(provider, authorizationCode))
			.willReturn(data);

		// when // then
		mockMvc.perform(get("/oauth/{provider}", provider.name().toLowerCase())
				.param("authorizationCode", authorizationCode))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
			.andExpect(jsonPath("$.result").value(ResponseResult.LOGIN_SUCCESS.getDescription()))
			.andExpect(jsonPath("$.data.code").value(data.getCode()))
			.andExpect(jsonPath("$.data.name").value(data.getName()))
			.andExpect(jsonPath("$.data.accessToken").value(data.getAccessToken()))
			.andExpect(jsonPath("$.data.refreshToken").value(data.getRefreshToken()))
			.andExpect(jsonPath("$.data.firstTime").value(data.isFirstTime()))
			.andDo(document("signup-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
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
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("Refresh Token"),
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

		given(authService.login(provider, authorizationCode))
			.willReturn(data);

		// when // then
		mockMvc.perform(get("/oauth/{provider}", provider.name().toLowerCase())
				.param("authorizationCode", authorizationCode))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
			.andExpect(jsonPath("$.result").value(ResponseResult.LOGIN_SUCCESS.getDescription()))
			.andExpect(jsonPath("$.data.code").value(data.getCode()))
			.andExpect(jsonPath("$.data.name").value(data.getName()))
			.andExpect(jsonPath("$.data.accessToken").value(data.getAccessToken()))
			.andExpect(jsonPath("$.data.refreshToken").value(data.getRefreshToken()))
			.andExpect(jsonPath("$.data.firstTime").value(data.isFirstTime()))
			.andDo(document("login-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
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
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("Refresh Token"),
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
		mockMvc.perform(get("/oauth/{provider}", "goooogle")
				.param("authorizationCode", authorizationCode))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
			.andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].type").value(InvalidProviderException.class.getSimpleName()))
			.andExpect(jsonPath("$.data[0].message").value(ErrorType.INVALID_PROVIDER.getMessage()))
			.andDo(document("login-fail-invalid-provider",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
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

		given(authService.login(provider, authorizationCode))
			.willThrow(new InvalidAuthorizationCodeException());

		// when // then
		mockMvc.perform(get("/oauth/{provider}", provider.name().toLowerCase())
				.param("authorizationCode", authorizationCode))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
			.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
			.andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].type").value(InvalidAuthorizationCodeException.class.getSimpleName()))
			.andExpect(jsonPath("$.data[0].message").value(ErrorType.INVALID_AUTHORIZATION_CODE.getMessage()))
			.andDo(document("login-fail-invalid-authorization-code",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
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

}
