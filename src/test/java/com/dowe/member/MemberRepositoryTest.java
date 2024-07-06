package com.dowe.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.dowe.IntegrationTestSupport;

class MemberRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void clean() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("Member를 저장할 때 [provider, authId] UNIQUE 제약 조건을 위반하면 예외가 발생한다")
	void testUniqueIndex1() {
		// given
		Member member1 = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("123456789")
			.name("name")
			.code("G12AB")
			.build();
		memberRepository.save(member1);

		Member member2 = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("123456789")
			.name("name")
			.code("G34CD")
			.build();

		// when / then
		assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("member.provider_auth_id");
	}

	@Test
	@DisplayName("Member를 저장할 때 [code] UNIQUE 제약 조건을 위반하면 예외가 발생한다")
	void testUniqueIndex2() {
		// given
		Member member1 = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("123456789")
			.name("name")
			.code("G12AB")
			.build();
		memberRepository.save(member1);

		Member member2 = Member.builder()
			.provider(Provider.GOOGLE)
			.authId("1234567890")
			.name("name")
			.code("G12AB")
			.build();

		// when / then
		assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("member.code");
	}

	@Test
	@DisplayName("[provider, authId]가 동일한 Member를 동시에 저장하면 1개만 정상적으로 저장한다")
	void testUniqueIndex3() throws InterruptedException {
		// when
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			int suffix = i;

			executorService.submit(() -> {
				try {
					Member member = Member.builder()
						.provider(Provider.GOOGLE)
						.authId("123456789")
						.name("name")
						.code("GAB1" + suffix)
						.build();

					memberRepository.save(member);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		List<Member> members = memberRepository.findAll();

		// then
		assertThat(members).hasSize(1);
	}

	@Test
	@DisplayName("[code]가 동일한 Member를 동시에 저장하면 1개만 정상적으로 저장한다")
	void testUniqueIndex4() throws InterruptedException {
		// when
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			int suffix = i;

			executorService.submit(() -> {
				try {
					Member member = Member.builder()
						.provider(Provider.GOOGLE)
						.authId("123456789" + suffix)
						.name("name")
						.code("G12AB")
						.build();

					memberRepository.save(member);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		List<Member> members = memberRepository.findAll();

		// then
		assertThat(members).hasSize(1);
	}

}
