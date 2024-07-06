package com.dowe.auth.application;

import static com.dowe.util.AppConstants.*;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.dowe.auth.MemberToken;
import com.dowe.auth.dto.TokenPair;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.config.properties.JwtProperties;
import com.dowe.exception.auth.ExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenManager {

	private final JwtProperties jwtProperties;
	private final MemberTokenRepository memberTokenRepository;

	public TokenPair issue(Long memberId) {
		Date now = new Date();
		Date accessTokenExpiredAt = new Date(now.getTime() + jwtProperties.getAccessTokenExpiry());
		Date refreshTokenExpiredAt = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiry());

		String accessToken = generateJwt(now, accessTokenExpiredAt, memberId);
		String refreshToken = generateJwt(now, refreshTokenExpiredAt, memberId);

		MemberToken memberToken = MemberToken.builder()
			.memberId(memberId)
			.refreshToken(refreshToken)
			.build();
		memberTokenRepository.save(memberToken);

		return new TokenPair(accessToken, refreshToken);
	}

	public Long parse(String token) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(jwtProperties.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
			return claims.get(MEMBER_ID, Long.class);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException();
		}
	}

	private String generateJwt(Date now, Date expiredAt, Long memberId) {
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer(jwtProperties.getIssuer())
			.setIssuedAt(now)
			.setExpiration(expiredAt)
			.claim(MEMBER_ID, memberId)
			.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
			.compact();
	}

}
