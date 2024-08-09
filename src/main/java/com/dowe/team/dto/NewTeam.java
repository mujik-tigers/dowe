package com.dowe.team.dto;

import lombok.Getter;

@Getter
public class NewTeam {

	private final Long teamId;

	public NewTeam(Long id) {
		this.teamId = id;
	}

}
