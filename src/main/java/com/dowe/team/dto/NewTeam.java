package com.dowe.team.dto;

import com.dowe.profile.Profile;
import com.dowe.team.Team;

import lombok.Getter;

@Getter
public class NewTeam {

	private final Long teamId;
	private final String teamTitle;
	private final String teamDescription;
	private final String teamImage;

	public NewTeam(Team team, Profile profile) {
		this.teamId = team.getId();
		this.teamTitle = team.getTitle();
		this.teamDescription = team.getDescription();
		this.teamImage = team.getImage();
	}

}
