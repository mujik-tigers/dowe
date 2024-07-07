package com.dowe.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.auth.dto.LoginData;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.application.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final OAuthProvider authProvider;
	private final MemberService memberService;
	private final TokenManager tokenManager;

	public LoginData login(Provider provider, String authorizationCode) {
		String authId = authProvider.authenticate(provider, authorizationCode);

		return generateLoginData(provider, authId);
	}

	public LoginData generateLoginData(Provider provider, String authId) {
		return memberService.findByProvider(provider, authId)
			.map(member -> LoginData.from(member, tokenManager.issue(member.getId()), false))
			.orElseGet(() -> {
				Member member = memberService.register(provider, authId);
				return LoginData.from(member, tokenManager.issue(member.getId()), true);
			});
	}

}
