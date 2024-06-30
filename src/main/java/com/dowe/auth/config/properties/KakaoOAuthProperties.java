package com.dowe.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("kakao")
@RequiredArgsConstructor
@Getter
public class KakaoOAuthProperties {

	private final String clientId;
	private final String redirectUri;
	private final String clientSecret;

}
