package com.dowe.team.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.member.Member;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.profile.Profile;
import com.dowe.profile.infrastructure.ProfileRepository;
import com.dowe.team.Team;
import com.dowe.team.dto.NewTeam;
import com.dowe.team.dto.TeamSettings;
import com.dowe.team.infrastructure.TeamRepository;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public NewTeam create(Long memberId, TeamSettings teamSettings) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberNotFoundException::new);

		Team teamBuilder = Team.builder()
			.title(StringUtil.removeExtraSpaces(teamSettings.getTitle()))
			.manager(member)
			.build();

		Team team = teamRepository.save(teamBuilder);
		Profile profile = team.join(member);
		profileRepository.save(profile);

		return new NewTeam(team, profile);
	}

}
