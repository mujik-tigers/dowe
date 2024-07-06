package com.dowe.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenPair {

	private final String accessToken;
	private final String refreshToken;

}
