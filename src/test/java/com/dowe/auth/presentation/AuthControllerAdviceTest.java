package com.dowe.auth.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.dowe.auth.application.OAuthProvider;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;
import com.dowe.util.api.ResponseResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerAdviceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberTokenRepository memberTokenRepository;

	@MockBean
	private OAuthProvider authProvider;

	@AfterEach
	void clean() {
		memberTokenRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("이미 등록된 사용자는 중복하여 등록하지 않고 로그인 처리한다")
	void handleMemberRegisterException() throws Exception {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name("name")
			.code("T1234")
			.build();
		memberRepository.save(member);

		given(authProvider.authenticate(provider, authorizationCode))
			.willReturn(authId);

		// when / then
		mockMvc.perform(get("/oauth/google")
				.param("authorizationCode", authorizationCode))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
			.andExpect(jsonPath("$.result").value(ResponseResult.LOGIN_SUCCESS.getDescription()))
			.andExpect(jsonPath("$.data.firstTime").value(false));
	}

}
