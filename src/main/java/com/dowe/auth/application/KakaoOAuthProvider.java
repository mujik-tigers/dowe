package com.dowe.auth.application;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.dowe.auth.config.properties.KakaoOAuthProperties;
import com.dowe.auth.dto.KakaoAccessToken;
import com.dowe.auth.dto.KakaoUserId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOAuthProvider {

	private final KakaoOAuthProperties property;
	private final RestClient restClient = RestClient.create();

	public Long authenticate(String authorizationCode) {
		KakaoAccessToken kakaoAccessToken = requestAccessToken(authorizationCode);
		KakaoUserId kakaoUserId = requestUserId(kakaoAccessToken.getAccessToken());

		return kakaoUserId.getId();
	}

	private KakaoAccessToken requestAccessToken(String authorizationCode) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", property.getClientId());
		body.add("redirect_uri", property.getRedirectUri());
		body.add("code", authorizationCode);
		body.add("client_secret", property.getClientSecret());

		return restClient.post()
			.uri("https://kauth.kakao.com/oauth/token")
			.header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
			.body(body)
			.retrieve()
			.toEntity(KakaoAccessToken.class)
			.getBody();
	}

	private KakaoUserId requestUserId(String accessToken) {
		return restClient.get()
			.uri("https://kapi.kakao.com/v2/user/me")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
			.retrieve()
			.toEntity(KakaoUserId.class)
			.getBody();
	}

}
