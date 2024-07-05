package com.dowe.auth.application;

import static com.dowe.util.RandomUtil.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.auth.dto.LoginData;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final OAuthProvider authProvider;
	private final MemberRepository memberRepository;
	private final TokenManager tokenManager;

	public LoginData login(Provider provider, String authorizationCode) {
		String authId = authProvider.authenticate(provider, authorizationCode);

		return memberRepository.findByProvider(provider, authId)
			.map(member -> LoginData.from(member, tokenManager.issue(member.getId()), false))
			.orElseGet(() -> {
				Member member = register(provider, authId);
				return LoginData.from(member, tokenManager.issue(member.getId()), true);
			});
	}

	private Member register(Provider provider, String authId) {
		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name(generateMemberName())
			.code(generateUniqueMemberCode(provider))
			.build();

		return memberRepository.save(member);        // TODO: (provider, authId), (code) 유니크 제약조건 -> 예외 발생 시 그냥 다시 로그인 하라고 예외 처리
	}

	private String generateUniqueMemberCode(Provider provider) {
		String code = generateMemberCode(provider);
		while (memberRepository.existsByCode(code)) {
			code = generateMemberCode(provider);
		}

		return code;
	}

}
