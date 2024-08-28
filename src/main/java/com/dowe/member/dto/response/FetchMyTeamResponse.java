package com.dowe.member.dto.response;

import java.util.List;

import com.dowe.member.dto.TeamOutline;

public record FetchMyTeamResponse(
	List<TeamOutline> teamOutlineList
) {

	public static FetchMyTeamResponse from(
		List<TeamOutline> teamOutlineList
	) {
		return new FetchMyTeamResponse(
			teamOutlineList
		);
	}

}
