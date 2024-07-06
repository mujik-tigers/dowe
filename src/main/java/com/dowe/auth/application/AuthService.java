package com.dowe.auth.application;

import static com.dowe.util.RandomUtil.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.dowe.auth.dto.LoginData;
import com.dowe.exception.CustomException;
import com.dowe.exception.ErrorType;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

		// TODO: 로그인 도중에 문제가 발생한 경우 클라이언트에게 재요청을 요구하지 않고 서버에서 재시도하도록 한다
		try {
			return memberRepository.save(member);
		} catch (DataIntegrityViolationException exception) {
			throw new CustomException(ErrorType.LOGIN_FAILED);
		}
	}

	private String generateUniqueMemberCode(Provider provider) {
		String code = generateMemberCode(provider);
		while (memberRepository.existsByCode(code)) {
			code = generateMemberCode(provider);
		}

		return code;
	}

}
