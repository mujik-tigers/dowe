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

	private final KakaoOAuthProvider kakaoOAuthProvider;
	private final MemberRepository memberRepository;

	public LoginMember loginByKakao(String authorizationCode) {
		Long kakaoUserId = kakaoOAuthProvider.authenticate(authorizationCode);
		LoginMember loginMember = memberRepository.findByProvider(Provider.KAKAO, kakaoUserId)
			.map(LoginMember::forReturning)
			.orElseGet(() -> LoginMember.forFirstTime(register(Provider.KAKAO, kakaoUserId)));

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
