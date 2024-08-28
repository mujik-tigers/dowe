package com.dowe.member.dto;

import static com.dowe.util.AppConstants.*;

import com.dowe.team.Team;

public record TeamOutline(
	Long id,
	String title,
	String image,
	int currentPeople,
	int maxPeople
) {

	public static TeamOutline of(
		Team team
	) {
		return new TeamOutline(
			team.getId(),
			team.getTitle(),
			team.getImage(),
			team.getProfiles().size(),
			TEAM_MAX_SIZE
		);
	}

}
