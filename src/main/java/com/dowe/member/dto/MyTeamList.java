package com.dowe.member.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class MyTeamList {

	private final List<TeamInList> teams;

	public MyTeamList(List<TeamInList> teams) {
		this.teams = teams;
	}

}
