package com.dowe.auth.application;

import static com.dowe.util.AppConstants.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dowe.IntegrationTestSupport;
import com.dowe.auth.MemberToken;
import com.dowe.auth.dto.TokenPair;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.config.properties.JwtProperties;
import com.dowe.exception.auth.InvalidTokenException;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

class TokenManagerTest extends IntegrationTestSupport {

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private MemberTokenRepository memberTokenRepository;

	@Autowired
	private JwtProperties jwtProperties;

	@BeforeEach
	void clear() {
		memberTokenRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("accessToken과 refreshToken이 잘 발급된다")
	void generateAccessToken() throws Exception {
		// given
		Long memberId = 1L;

		// when
		TokenPair tokenPair = tokenManager.issue(memberId);

		// then
		Long memberIdInAccessToken = tokenManager.parse(tokenPair.getAccessToken());
		Long memberIdInRefreshToken = tokenManager.parse(tokenPair.getRefreshToken());

		assertThat(memberId).isEqualTo(memberIdInAccessToken);
		assertThat(memberId).isEqualTo(memberIdInRefreshToken);
	}

	@Test
	@DisplayName("회원가입한 유저는 id와 refreshToken을 매핑한 memberToken이 생긴다")
	void generateMemberToken() throws Exception {
		// given
		Long memberId = 1L;

		// when
		TokenPair issue = tokenManager.issue(memberId);

		// then
		MemberToken memberToken = memberTokenRepository.findAll().get(0);

		assertThat(memberToken.getMemberId()).isEqualTo(memberId);
		assertThat(memberToken.getRefreshToken()).isEqualTo(issue.getRefreshToken());
	}

	@Test
	@DisplayName("로그인을 통해 refreshToken을 재발급하면, memberToken의 refreshToken도 재발급한 값으로 바뀐다")
	void updateMemberTokenViaLogin() throws Exception {
		// given
		Long memberId = 1L;

		tokenManager.issue(memberId);
		String previousRefreshToken = memberTokenRepository.findAll().get(0).getRefreshToken();

		// when
		Thread.sleep(1000);
		TokenPair issue = tokenManager.issue(memberId);

		// then
		assertThat(memberTokenRepository.findAll().size()).isEqualTo(1);

		MemberToken memberToken = memberTokenRepository.findAll().get(0);
		assertThat(memberToken.getRefreshToken()).isEqualTo(issue.getRefreshToken());
		assertThat(memberToken.getRefreshToken()).isNotEqualTo(previousRefreshToken);
	}

	@Test
	@DisplayName("만료시간이 지난 토큰을 파싱하면 예외가 발생한다")
	void parseExpiredToken() throws Exception {
		// given
		Date now = new Date();
		String expiredToken = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer(jwtProperties.getIssuer())
			.setIssuedAt(now)
			.setExpiration(now)
			.claim(MEMBER_ID, 1L)
			.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
			.compact();

		// when // then
		assertThatThrownBy(() -> tokenManager.parse(expiredToken))
			.isInstanceOf(InvalidTokenException.class);
	}

}
