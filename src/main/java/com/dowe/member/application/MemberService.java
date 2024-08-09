package com.dowe.member.application;

import static com.dowe.util.RandomUtil.*;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.exception.member.MemberRegisterException;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.dto.MemberName;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final MemberCodeStorage memberCodeStorage;

	public Optional<Member> findBy(Provider provider, String authId) {
		return memberRepository.findByProvider(provider, authId);
	}

	@Transactional
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
		while (!memberCodeStorage.saveMemberCodeIfUnique(code)) {
			code = generateMemberCode(provider);
		}

		return code;
	}

	@Transactional
	public MemberName updateName(Long memberId, String newName) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow();

		newName = StringUtil.removeExtraSpaces(newName);
		member.updateName(newName);

		return new MemberName(newName);
	}

}
