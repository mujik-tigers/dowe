package com.dowe.exception.member;

import com.dowe.member.Provider;

import lombok.Getter;

@Getter
public class MemberRegisterException extends RuntimeException {

	private final Provider provider;
	private final String authId;

	public MemberRegisterException(Provider provider, String authId) {
		this.provider = provider;
		this.authId = authId;
	}

}
