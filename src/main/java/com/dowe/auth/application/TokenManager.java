package com.dowe.auth.application;

import static com.dowe.exception.auth.TokenType.*;
import static com.dowe.util.AppConstants.*;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.dowe.auth.MemberToken;
import com.dowe.auth.dto.TokenPair;
import com.dowe.auth.infrastructure.MemberTokenRepository;
import com.dowe.config.properties.JwtProperties;
import com.dowe.exception.auth.ExpiredTokenException;
import com.dowe.exception.auth.InvalidTokenException;
import com.dowe.exception.auth.TokenType;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
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

		String accessToken = generateJwt(now, accessTokenExpiredAt, memberId, ACCESS_TOKEN);
		String refreshToken = generateJwt(now, refreshTokenExpiredAt, memberId, REFRESH_TOKEN);

		MemberToken memberToken = MemberToken.builder()
			.memberId(memberId)
			.refreshToken(refreshToken)
			.build();
		memberTokenRepository.save(memberToken);

		return new TokenPair(accessToken, refreshToken);
	}

	public Long parse(String token, TokenType type) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(jwtProperties.getSecretKey())
				.parseClaimsJws(token)
				.getBody();

			if (getTokenTypeFromClaims(claims, TOKEN_TYPE) != type) {
				throw new InvalidTokenException(type);
			}

			return claims.get(MEMBER_ID, Long.class);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(type);
		} catch (MalformedJwtException | SignatureException | ClaimJwtException e) {
			throw new InvalidTokenException(type);
		}
	}

	public TokenPair refresh(String refreshToken) {
		Long memberId = parse(refreshToken, REFRESH_TOKEN);
		MemberToken memberToken = memberTokenRepository.findById(memberId)
			.orElseThrow(() -> new InvalidTokenException(REFRESH_TOKEN));

		if (!memberToken.getRefreshToken().equals(refreshToken)) {
			throw new InvalidTokenException(REFRESH_TOKEN);
		}

		return issue(memberId);
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

	private TokenType getTokenTypeFromClaims(Claims claims, String key) {
		String type = claims.get(key, String.class);
		return TokenType.valueOf(type);
	}

}
