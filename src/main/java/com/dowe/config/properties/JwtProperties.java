package com.dowe.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("jwt")
@RequiredArgsConstructor
@Getter
public class JwtProperties {

	private final String issuer;
	private final String secretKey;
	private final Long accessTokenExpiry;
	private final Long refreshTokenExpiry;

}
