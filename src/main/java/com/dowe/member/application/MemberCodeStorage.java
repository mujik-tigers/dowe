package com.dowe.member.application;

import static com.dowe.util.AppConstants.*;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberCodeStorage {

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisScript<Boolean> saveMemberCodeIfUniqueScript;

	public Boolean saveMemberCodeIfUnique(String newMemberCode) {
		return redisTemplate.execute(saveMemberCodeIfUniqueScript, List.of(MEMBER_CODE_SET), newMemberCode);
	}

}
