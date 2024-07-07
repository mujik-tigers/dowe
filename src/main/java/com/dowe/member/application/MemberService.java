package com.dowe.member.application;

import static com.dowe.util.RandomUtil.*;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.dowe.exception.member.MemberRegisterException;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.infrastructure.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Optional<Member> findByProvider(Provider provider, String authId) {
		return memberRepository.findByProvider(provider, authId);
	}

	public Member register(Provider provider, String authId) {
		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name(generateMemberName())
			.code(generateUniqueMemberCode(provider))
			.build();

		try {
			return memberRepository.save(member);
		} catch (DataIntegrityViolationException exception) {
			throw new MemberRegisterException(provider, authId);
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
