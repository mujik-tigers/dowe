package com.dowe.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dowe.IntegrationTestSupport;
import com.dowe.auth.dto.LoginData;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;

class AuthServiceTest extends IntegrationTestSupport {

	@Autowired
	private AuthService authService;

	@Autowired
	private OAuthProvider authProvider;

	@Autowired
	private MemberTokenRepository memberTokenRepository;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void clean() {
		memberTokenRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("처음 로그인한 경우 isFirstTime 값은 true이다")
	void testIsFirstTime1() {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		doReturn(authId).when(authProvider).authenticate(provider, authorizationCode);

		// when
		LoginData loginData = authService.login(provider, authorizationCode);

		// then
		assertThat(loginData.isFirstTime()).isTrue();
	}

	@Test
	@DisplayName("다시 로그인한 경우 isFirstTime 값은 false이다")
	void testIsFirstTime2() {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		doReturn(authId).when(authProvider).authenticate(provider, authorizationCode);

		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name("name")
			.code("T1234")
			.build();
		memberRepository.save(member);

		// when
		LoginData loginData = authService.login(provider, authorizationCode);

		// then
		assertThat(loginData.isFirstTime()).isFalse();
	}

	@Test
	@DisplayName("이미 등록된 사용자를 또 등록하려고 하는 경우 예외가 발생한다")
	void testRegister() {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		doReturn(authId).when(authProvider).authenticate(provider, authorizationCode);

		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name("name")
			.code("T1234")
			.build();
		memberRepository.save(member);

		doReturn(Optional.empty()).when(memberRepository).findByProvider(provider, authId);

		// when / then
		assertThatThrownBy(() -> authService.login(provider, authorizationCode))
			.isInstanceOf(CustomException.class)
			.hasMessage(ErrorType.LOGIN_FAILED.getMessage());
	}

}
