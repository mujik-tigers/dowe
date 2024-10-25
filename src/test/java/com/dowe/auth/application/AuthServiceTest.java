package com.dowe.auth.application;

import static com.dowe.util.AppConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dowe.IntegrationTestSupport;
import com.dowe.auth.dto.LoginData;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.exception.member.MemberRegisterException;
import com.dowe.member.Member;
import com.dowe.member.Provider;

class AuthServiceTest extends IntegrationTestSupport {

	@Autowired
	private AuthService authService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MemberTokenRepository memberTokenRepository;

	@BeforeEach
	void clean() {
		memberTokenRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		redisTemplate.delete(MEMBER_CODE_SET);
	}

	@Test
	@DisplayName("처음 로그인한 경우 firstTime 값은 true이다")
	void testFirstTime1() {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		doReturn(authId).when(authProvider).authenticate(FRONTEND_ORIGIN, provider, authorizationCode);

		// when
		LoginData loginData = authService.login(FRONTEND_ORIGIN, provider, authorizationCode);

		// then
		assertThat(loginData.isFirstTime()).isTrue();
	}

	@Test
	@DisplayName("다시 로그인한 경우 firstTime 값은 false이다")
	void testFirstTime2() {
		// given
		String origin = "https://dowith.today";
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "test auth id";

		doReturn(authId).when(authProvider).authenticate(origin, provider, authorizationCode);

		Member member = Member.builder()
			.provider(provider)
			.authId(authId)
			.name("name")
			.code("T1234")
			.build();
		memberRepository.save(member);

		// when
		LoginData loginData = authService.login(origin, provider, authorizationCode);

		// then
		assertThat(loginData.isFirstTime()).isFalse();
	}

	@Test
	@DisplayName("이미 등록된 사용자를 또 등록하려고 하는 경우 예외가 발생한다")
	void testRegister() throws InterruptedException {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "123456789";

		doReturn(authId).when(authProvider).authenticate(FRONTEND_ORIGIN, provider, authorizationCode);
		doReturn(Optional.empty()).when(memberRepository).findByProvider(provider, authId);

		// when
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		CountDownLatch latch = new CountDownLatch(threadCount);

		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failureCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					authService.login(FRONTEND_ORIGIN, provider, authorizationCode);
					successCount.incrementAndGet();
				} catch (MemberRegisterException exception) {
					failureCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		List<Member> members = memberRepository.findAll();

		// then
		assertThat(members).hasSize(1);
		assertThat(successCount.get()).isEqualTo(1);
		assertThat(failureCount.get()).isEqualTo(threadCount - 1);
	}

}
