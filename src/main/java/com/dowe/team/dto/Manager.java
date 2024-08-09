package com.dowe.team.dto;

import com.dowe.profile.Profile;

import lombok.Getter;

@Getter
public class Manager {

	private final Long id;
	private final String image;
	private final String nickname;

	public Manager(Profile profile) {
		this.id = profile.getId();
		this.image = profile.getImage();
		this.nickname = profile.getNickname();
	}

}
