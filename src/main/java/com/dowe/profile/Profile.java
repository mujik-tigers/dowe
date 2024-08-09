package com.dowe.profile;

import com.dowe.member.Member;
import com.dowe.team.Team;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private String image;
	private String nickname;
	private String description;

	@Builder
	public Profile(Team team, Member member, String image, String nickname, String description) {
		this.team = team;
		this.member = member;
		this.image = image;
		this.nickname = nickname;
		this.description = description;
	}

	public boolean isOwnedBy(Member member) {
		return this.member.equals(member);
	}

	public static Profile of(Team team, Member member) {
		return Profile.builder()
			.team(team)
			.member(member)
			.nickname(member.getName())
			.build();
	}

}
