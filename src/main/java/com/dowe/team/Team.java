package com.dowe.team;

import java.util.ArrayList;
import java.util.List;

import com.dowe.exception.team.AlreadyInTeamException;
import com.dowe.exception.team.TeamFullException;
import com.dowe.member.Member;
import com.dowe.profile.Profile;
import com.dowe.util.AppConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String description;
	private String image;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Member manager;

	private String notice;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
	private List<Profile> profiles = new ArrayList<>();

	@Builder
	public Team(String title, String image, Member manager, String notice) {
		this.title = title;
		this.image = image;
		this.manager = manager;
		this.notice = notice;
	}

	public Profile join(Member member) {
		if (profiles.size() >= AppConstants.TEAM_MAX_SIZE) {
			throw new TeamFullException();
		}

		for (Profile profile : this.profiles) {
			if (profile.isOwnedBy(member)) {
				throw new AlreadyInTeamException();
			}
		}

		Profile profile = Profile.of(this, member);
		profiles.add(profile);

		return profile;
	}

}
