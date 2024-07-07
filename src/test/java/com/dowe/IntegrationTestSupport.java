package com.dowe;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import com.dowe.auth.application.OAuthProvider;
import com.dowe.member.infrastructure.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

	@SpyBean
	protected OAuthProvider authProvider;

	@SpyBean
	protected MemberRepository memberRepository;

}
