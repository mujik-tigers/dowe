package com.dowe.auth.dto;

import com.dowe.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginData {

	private final String code;
	private final String name;
	private final boolean isFirstTime;
	private final String accessToken;
	private final String refreshToken;

	@Builder
	public LoginData(String code, String name, boolean isFirstTime, String accessToken, String refreshToken) {
		this.code = code;
		this.name = name;
		this.isFirstTime = isFirstTime;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static LoginData from(Member member, TokenPair tokens, boolean isFirstTime) {
		return LoginData.builder()
			.code(member.getCode())
			.name(member.getName())
			.isFirstTime(isFirstTime)
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}

}
