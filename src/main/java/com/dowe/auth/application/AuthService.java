package com.dowe.auth.application;

import org.springframework.stereotype.Service;

import com.dowe.auth.dto.LoginMember;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;
import com.dowe.util.RandomUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final OAuthProvider authProvider;
	private final MemberRepository memberRepository;

	public LoginMember login(Provider provider, String authorizationCode) {
		String authId = authProvider.authenticate(provider, authorizationCode);
		LoginMember loginMember = memberRepository.findByProvider(provider, authId)
			.map(LoginMember::forReturning)
			.orElseGet(() -> LoginMember.forFirstTime(register(provider, authId)));

		return loginMember;
	}

	private Member register(Provider provider, String authId) {
		String code = generateCode(provider);
		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name("iam" + code)
			.code(code)
			.build();

		return memberRepository.save(member);
	}

	private String generateCode(Provider provider) {
		return provider.getSignature() + RandomUtil.generateRandomCode();
	}

}
