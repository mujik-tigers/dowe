package com.dowe.auth.application;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.dowe.auth.dto.AccessToken;
import com.dowe.auth.dto.UserResource;
import com.dowe.config.properties.OAuthProperties;
import com.dowe.member.Provider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthProvider {

	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String BEARER = "Bearer ";
	private static final String X_WWW_FORM_URLENCODED_CHARSET_UTF_8 = "application/x-www-form-urlencoded;charset=utf-8";

	private final OAuthProperties properties;
	private final RestClient restClient;

	public String authenticate(Provider provider, String authorizationCode) {
		AccessToken accessToken = requestAccessToken(provider, authorizationCode);
		UserResource userResource = requestUserId(provider, accessToken.getAccessToken());

		return userResource.getId();
	}

	private AccessToken requestAccessToken(Provider provider, String authorizationCode) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", AUTHORIZATION_CODE);
		body.add("client_id", properties.getClientId(provider));
		body.add("redirect_uri", properties.getRedirectUri(provider));
		body.add("code", authorizationCode);
		body.add("client_secret", properties.getClientSecret(provider));

		return restClient.post()
			.uri(properties.getTokenRequestUri(provider))
			.header(HttpHeaders.CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8)
			.body(body)
			.retrieve()
			.toEntity(AccessToken.class)
			.getBody();
	}

	private UserResource requestUserId(Provider provider, String accessToken) {
		return restClient.get()
			.uri(properties.getResourceRequestUri(provider))
			.header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
			.header(HttpHeaders.CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8)
			.retrieve()
			.toEntity(UserResource.class)
			.getBody();
	}

}
