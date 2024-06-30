package com.dowe.auth.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.dowe.auth.dto.AccessToken;
import com.dowe.auth.dto.AccessTokenRequestParameter;
import com.dowe.auth.dto.UserResource;
import com.dowe.auth.property.GoogleOAuthProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

	private final GoogleOAuthProperties oAuthProperties;
	private final RestClient restClient;

	public void login(String code) {
		String accessToken = getAccessToken(code);
		log.info("accessToken = {}", accessToken);
		String userId = getUserResource(accessToken);
		log.info("userResource = {}", userId);
	}

	private String getAccessToken(String authorizationCode) {
		AccessTokenRequestParameter parameter = AccessTokenRequestParameter.builder()
			.code(authorizationCode)
			.clientId(oAuthProperties.getClientId())
			.clientSecret(oAuthProperties.getClientSecret())
			.redirectUri(oAuthProperties.getRedirectUrl())
			.grantType("authorization_code")
			.build();

		return restClient.post()
			.uri(oAuthProperties.getTokenRequestUrl())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(parameter)
			.retrieve()
			.body(AccessToken.class)
			.getAccessToken();
	}

	private String getUserResource(String accessToken) {
		return restClient.get()
			.uri(oAuthProperties.getResourceRequestUrl())
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.body(UserResource.class)
			.getId();
	}

}
