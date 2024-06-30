package com.dowe.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccessToken {

	@JsonProperty("access_token")
	private String accessToken;

}
