package com.dowe.config.properties;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.dowe.member.Provider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("oauth")
@RequiredArgsConstructor
public class OAuthProperties {

	private final Map<Provider, Properties> properties;
	private final List<String> allowedOrigins;

	public String getClientIdOf(Provider provider) {
		return properties.get(provider).clientId;
	}

	public String getClientSecretOf(Provider provider) {
		return properties.get(provider).clientSecret;
	}

	public String getRedirectBaseUriOf(Provider provider) {
		return properties.get(provider).redirectBaseUri;
	}

	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}

	public String getTokenRequestUriOf(Provider provider) {
		return properties.get(provider).tokenRequestUri;
	}

	public String getResourceRequestUriOf(Provider provider) {
		return properties.get(provider).resourceRequestUri;
	}

	@AllArgsConstructor
	public static class Properties {

		private final String clientId;
		private final String clientSecret;
		private final String redirectBaseUri;
		private final String tokenRequestUri;
		private final String resourceRequestUri;

	}

}
