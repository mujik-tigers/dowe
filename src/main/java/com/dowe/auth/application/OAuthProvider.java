package com.dowe.auth.application;

import static com.dowe.util.AppConstants.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.dowe.auth.dto.AccessToken;
import com.dowe.auth.dto.UserResource;
import com.dowe.config.properties.OAuthProperties;
import com.dowe.exception.InvalidAuthorizationCodeException;
import com.dowe.member.Provider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthProvider {

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
		body.add("client_id", properties.getClientIdOf(provider));
		body.add("redirect_uri", properties.getRedirectUriOf(provider));
		body.add("code", authorizationCode);
		body.add("client_secret", properties.getClientSecretOf(provider));

		return restClient.post()
			.uri(properties.getTokenRequestUriOf(provider))
			.header(HttpHeaders.CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8)
			.body(body)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
				throw new InvalidAuthorizationCodeException();
			})
			.toEntity(AccessToken.class)
			.getBody();
	}

	private UserResource requestUserId(Provider provider, String accessToken) {
		return restClient.get()
			.uri(properties.getResourceRequestUriOf(provider))
			.header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
			.header(HttpHeaders.CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8)
			.retrieve()
			.toEntity(UserResource.class)
			.getBody();
	}

}
