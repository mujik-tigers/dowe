package com.dowe.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccessTokenRequestParameter {

	private String code;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String grantType;

	@Builder
	public AccessTokenRequestParameter(String code, String clientId, String clientSecret, String redirectUri, String grantType) {
		this.code = code;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.grantType = grantType;
	}

}
