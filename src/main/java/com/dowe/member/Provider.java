package com.dowe.member;

import lombok.Getter;

@Getter
public enum Provider {

	GOOGLE("G"), KAKAO("K");

	private final String signature;

	Provider(String signature) {
		this.signature = signature;
	}

}
