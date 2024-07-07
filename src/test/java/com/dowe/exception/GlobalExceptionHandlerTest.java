package com.dowe.exception;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dowe.auth.application.OAuthProvider;
import com.dowe.auth.dto.LoginData;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.member.Member;
import com.dowe.member.MemberRepository;
import com.dowe.member.Provider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OAuthProvider authProvider;

	@Autowired
	private MemberTokenRepository memberTokenRepository;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void clean() {
		memberTokenRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("MemberRegisterException 발생 시 재시도하여 정상 로그인 처리한다")
	void handleMemberRegisterException() throws InterruptedException {
		// given
		Provider provider = Provider.GOOGLE;
		String authorizationCode = "test authorization code";
		String authId = "123456789";

		doReturn(authId).when(authProvider).authenticate(provider, authorizationCode);

		// when
		int threadCount = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		AtomicInteger firstTimeTrueCount = new AtomicInteger(0);
		AtomicInteger firstTimeFalseCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					ResultActions resultActions = mockMvc.perform(get("/oauth/google")
							.param("authorizationCode", authorizationCode))
						.andExpect(status().isOk());

					String response = resultActions.andReturn().getResponse().getContentAsString(Charsets.UTF_8);
					JsonNode rootNode = objectMapper.readTree(response);
					LoginData loginData = objectMapper.treeToValue(rootNode.get("data"), LoginData.class);

					if (loginData.isFirstTime()) {
						firstTimeTrueCount.incrementAndGet();
					} else {
						firstTimeFalseCount.incrementAndGet();
					}
				} catch (Exception e) {
					e.printStackTrace();
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
		assertThat(firstTimeTrueCount.get()).isEqualTo(1);
		assertThat(firstTimeFalseCount.get()).isEqualTo(threadCount - 1);
	}

}
