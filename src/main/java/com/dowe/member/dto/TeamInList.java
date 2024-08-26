package com.dowe.member.dto;

import com.dowe.team.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamInList {

	private final Long id;
	private final String title;
	private final String description;
	private final String image;

	public TeamInList(Team team) {
		this.id = team.getId();
		this.title = team.getTitle();
		this.description = team.getDescription();
		this.image = team.getImage();
	}

}
