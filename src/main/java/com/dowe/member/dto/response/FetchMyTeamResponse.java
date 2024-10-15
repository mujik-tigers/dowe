package com.dowe.member.dto.response;

import java.util.List;

import com.dowe.team.dto.TeamOutline;

public record FetchMyTeamResponse(
	List<TeamOutline> teamOutlines
) {

	public static FetchMyTeamResponse from(
		List<TeamOutline> teamOutlines
	) {
		return new FetchMyTeamResponse(
			teamOutlines
		);
	}

}
