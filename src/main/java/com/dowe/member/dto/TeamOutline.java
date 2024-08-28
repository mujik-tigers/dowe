package com.dowe.member.dto;

public record TeamOutline(
	Long id,
	String title,
	String image,
	int currentPeople,
	int maxPeople
) {

	public static TeamOutline of(
		Long id,
		String title,
		String image,
		int currentPeople,
		int maxPeople
	) {
		return new TeamOutline(
			id,
			title,
			image,
			currentPeople,
			maxPeople
		);
	}

}
