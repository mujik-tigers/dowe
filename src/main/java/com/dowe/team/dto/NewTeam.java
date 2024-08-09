package com.dowe.team.dto;

import com.dowe.team.Team;

import lombok.Getter;

@Getter
public class NewTeam {

	private final Long teamId;

	public NewTeam(Team team) {
		this.teamId = team.getId();
	}

}
