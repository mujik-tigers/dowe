package com.dowe.auth.application;

import static com.dowe.auth.TokenType.*;
import static com.dowe.util.AppConstants.*;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.auth.MemberToken;
import com.dowe.auth.TokenType;
import com.dowe.auth.dto.TokenPair;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.config.properties.JwtProperties;
import com.dowe.exception.auth.ExpiredTokenException;
import com.dowe.exception.auth.InvalidTokenException;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TokenManager {

	private final JwtProperties jwtProperties;
	private final MemberTokenRepository memberTokenRepository;

	public TokenPair issue(Long memberId) {
		TokenPair tokenPair = generateTokenPair(memberId);

		MemberToken memberToken = MemberToken.builder()
			.memberId(memberId)
			.refreshToken(tokenPair.getRefreshToken())
			.build();
		memberTokenRepository.save(memberToken);

		return tokenPair;
	}

	public Long parse(String token, TokenType needTokenType) {
		TokenType currentTokenType = null;

		try {
			Claims claims = Jwts.parser()
				.setSigningKey(jwtProperties.getSecretKey())
				.parseClaimsJws(token)
				.getBody();

			currentTokenType = getTokenTypeFromClaims(claims);
			if (currentTokenType != needTokenType) {
				throw new InvalidTokenException(currentTokenType, needTokenType);
			}

			return claims.get(MEMBER_ID, Long.class);
		} catch (ExpiredJwtException e) {
			currentTokenType = getTokenTypeFromClaims(e.getClaims());
			log.info("TokenManager parse()");
			log.info("currentTokenType: {}", currentTokenType);
			throw new ExpiredTokenException(currentTokenType, needTokenType);
		} catch (MalformedJwtException | SignatureException | ClaimJwtException e) {
			throw new InvalidTokenException(FAKE, needTokenType);
		}
	}

	public TokenPair refresh(Long memberId, String refreshTokenInput) {
		MemberToken memberToken = memberTokenRepository.findById(memberId)
			.orElseThrow(() -> new InvalidTokenException(REFRESH, REFRESH));

		if (memberToken.doesNotMatch(refreshTokenInput)) {
			throw new InvalidTokenException(REFRESH, REFRESH);
		}

		TokenPair tokenPair = generateTokenPair(memberId);
		memberToken.changeRefreshToken(tokenPair.getRefreshToken());

		return tokenPair;
	}

	private String generateJwt(Date now, Date expiredAt, Long memberId, TokenType type) {
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer(jwtProperties.getIssuer())
			.setIssuedAt(now)
			.setExpiration(expiredAt)
			.claim(MEMBER_ID, memberId)
			.claim(TOKEN_TYPE, type)
			.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
			.compact();
	}

	private TokenType getTokenTypeFromClaims(Claims claims) {
		String type = claims.get(TOKEN_TYPE, String.class);

		return TokenType.valueOf(type);
	}

	private TokenPair generateTokenPair(Long memberId) {
		Date now = new Date();
		Date accessTokenExpiredAt = new Date(now.getTime() + jwtProperties.getAccessTokenExpiry());
		Date refreshTokenExpiredAt = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiry());

		String accessToken = generateJwt(now, accessTokenExpiredAt, memberId, ACCESS);
		String refreshToken = generateJwt(now, refreshTokenExpiredAt, memberId, REFRESH);

		return new TokenPair(accessToken, refreshToken);
	}

}
