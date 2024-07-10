package com.dowe.auth.dto;

import com.dowe.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginData {

	private final String code;
	private final String name;
	private final boolean firstTime;
	private final String accessToken;
	private final String refreshToken;

	@Builder
	public LoginData(String code, String name, boolean firstTime, String accessToken, String refreshToken) {
		this.code = code;
		this.name = name;
		this.firstTime = firstTime;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static LoginData from(Member member, TokenPair tokens, boolean isFirstTime) {
		return LoginData.builder()
			.code(member.getCode())
			.name(member.getName())
			.firstTime(isFirstTime)
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.build();
	}

}
