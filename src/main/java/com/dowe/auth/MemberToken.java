package com.dowe.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberToken {

	@Id
	private Long memberId;

	private String refreshToken;

	@Builder
	public MemberToken(Long memberId, String refreshToken) {
		this.memberId = memberId;
		this.refreshToken = refreshToken;
	}

	public void changeRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean doesNotMatch(String refreshTokenInput) {
		return !refreshToken.equals(refreshTokenInput);
	}

}
