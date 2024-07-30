package com.dowe.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dowe.IntegrationTestSupport;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.dto.MemberName;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@BeforeEach
	void clean() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("새로운 이름으로 변경한다")
	void updateName() {
		// given
		Member member = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("123456789")
			.name("name")
			.code("G12AB")
			.build();
		memberRepository.save(member);

		String newName = "new name";

		// when
		MemberName memberName = memberService.updateName(member.getId(), newName);

		// then
		Member updatedMember = memberRepository.findById(member.getId()).get();

		assertThat(updatedMember.getName()).isEqualTo(memberName.getNewName());
	}

	@ParameterizedTest
	@CsvSource(value = {" my name :my name", " oh   my   name :oh my name"}, delimiter = ':')
	@DisplayName("이름 양 끝의 공백은 제거되고 연속된 공백은 하나의 공백으로 변환된다")
	void removeExtraSpaces(String newName, String result) {
		// given
		Member member = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("123456789")
			.name("name")
			.code("G12AB")
			.build();
		memberRepository.save(member);

		// when
		memberService.updateName(member.getId(), newName);

		// then
		Member updatedMember = memberRepository.findById(member.getId()).get();

		assertThat(updatedMember.getName()).isEqualTo(result);
	}

}
