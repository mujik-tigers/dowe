package com.dowe.util;

import static com.dowe.util.AppConstants.*;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.dowe.member.infrastructure.MemberRepository;

import lombok.RequiredArgsConstructor;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

	private final MemberRepository memberRepository;
	private final StringRedisTemplate redisTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> codes = memberRepository.findAllOnlyCode();
		if (!codes.isEmpty()) {
			redisTemplate.delete(MEMBER_CODE_SET);
			redisTemplate.opsForSet().add(MEMBER_CODE_SET, codes.toArray(String[]::new));
		}
	}

}
