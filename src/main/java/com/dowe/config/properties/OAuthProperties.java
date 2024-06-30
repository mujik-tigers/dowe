package com.dowe.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.dowe.member.Provider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("oauth")
@RequiredArgsConstructor
public class OAuthProperties {

	private final Map<Provider, Properties> properties;

	public String getClientId(Provider provider) {
		return properties.get(provider).clientId;
	}

	public String getClientSecret(Provider provider) {
		return properties.get(provider).clientSecret;
	}

	public String getRedirectUri(Provider provider) {
		return properties.get(provider).redirectUri;
	}

	public String getTokenRequestUri(Provider provider) {
		return properties.get(provider).tokenRequestUri;
	}

	public String getResourceRequestUri(Provider provider) {
		return properties.get(provider).resourceRequestUri;
	}

	@AllArgsConstructor
	public static class Properties {

		private final String clientId;
		private final String clientSecret;
		private final String redirectUri;
		private final String tokenRequestUri;
		private final String resourceRequestUri;

	}

}
