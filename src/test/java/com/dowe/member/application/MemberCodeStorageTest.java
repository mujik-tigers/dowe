package com.dowe.member.application;

import static com.dowe.TestConstants.MEMBER_CODE_SET;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dowe.IntegrationTestSupport;

class MemberCodeStorageTest extends IntegrationTestSupport {

	@Autowired
	private MemberCodeStorage memberCodeStorage;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@BeforeEach
	void clean() {
		redisTemplate.delete(MEMBER_CODE_SET);
	}

	@Test
	@DisplayName("저장하려는 MemberCode가 존재하지 않는 MemberCode라면 저장 후 true를 반환한다")
	void save() throws Exception {
		// given
		String memberCode = "K1234";

		// when
		Boolean saveFlag = memberCodeStorage.saveMemberCodeIfUnique(memberCode);

		// then
		assertThat(saveFlag).isTrue();
		Set<String> codes = redisTemplate.opsForSet().members(MEMBER_CODE_SET);
		assertThat(codes).hasSize(1);
		assertThat(codes).containsExactly(memberCode);
	}

	@Test
	@DisplayName("저장하려는 MemberCode가 이미 존재하는 MemberCode라면 false를 반환한다")
	void saveFail() throws Exception {
		// given
		String memberCode = "K1234";
		memberCodeStorage.saveMemberCodeIfUnique(memberCode);

		// when
		Boolean saveFlag = memberCodeStorage.saveMemberCodeIfUnique(memberCode);

		// then
		assertThat(saveFlag).isFalse();
		Set<String> codes = redisTemplate.opsForSet().members(MEMBER_CODE_SET);
		assertThat(codes).hasSize(1);
	}

}
