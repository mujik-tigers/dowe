package com.dowe.auth.dto;

import com.dowe.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginMember {

	private final Long id;
	private final String name;
	private final boolean isFirstTime;

	public static LoginMember forFirstTime(Member member) {
		return LoginMember.builder()
			.id(member.getId())
			.name(member.getName())
			.isFirstTime(true)
			.build();
	}

	public static LoginMember forReturning(Member member) {
		return LoginMember.builder()
			.id(member.getId())
			.name(member.getName())
			.isFirstTime(false)
			.build();
	}

	@Builder
	public LoginMember(Long id, String name, boolean isFirstTime) {
		this.id = id;
		this.name = name;
		this.isFirstTime = isFirstTime;
	}

}
