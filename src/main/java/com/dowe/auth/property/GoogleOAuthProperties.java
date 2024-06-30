package com.dowe.auth.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("oauth.google")
@RequiredArgsConstructor
@Getter
public class GoogleOAuthProperties {

	private final String clientId;
	private final String clientSecret;
	private final String redirectUrl;
	private final String tokenRequestUrl;
	private final String resourceRequestUrl;

}
