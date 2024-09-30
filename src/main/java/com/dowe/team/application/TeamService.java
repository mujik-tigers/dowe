package com.dowe.team.application;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.elasticsearch.dto.response.FindByTeamTitleResponse;
import com.dowe.elasticsearch.infrastructure.TeamDocumentRepository;
import com.dowe.team.dto.TeamOutline;
import java.util.ArrayList;
import java.util.List;
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
import com.dowe.util.S3Uploader;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

  private static final String TEAM_IMAGE_DIRECTORY = "team";

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final ProfileRepository profileRepository;
  private final S3Uploader s3Uploader;

  private final TeamDocumentRepository teamDocumentRepository;

  @Transactional
  public NewTeam create(Long memberId, TeamSettings teamSettings) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String image = null;
    if (teamSettings.getImage() != null && !teamSettings.getImage().isEmpty()) {
      image = s3Uploader.upload(TEAM_IMAGE_DIRECTORY, teamSettings.getImage());
    }

    Team teamBuilder = Team.builder()
        .title(StringUtil.removeExtraSpaces(teamSettings.getTitle()))
        .description(StringUtil.removeExtraSpaces(teamSettings.getDescription()))
        .image(image)
        .manager(member)
        .build();

    Team team = teamRepository.save(teamBuilder);
    Profile profile = team.join(member);
    profileRepository.save(profile);

    return new NewTeam(team.getId());
  }

  public FindByTeamTitleResponse findByTeamTitle(
      String teamTitle
  ) {
    List<TeamDocument> findTeamList = teamDocumentRepository.findByTitle(teamTitle);

    List<TeamOutline> teamOutlineList = new ArrayList<>();

    for (TeamDocument teamDocument : findTeamList) {
      Long teamId = Long.valueOf(teamDocument.getId());
      teamRepository.findById(teamId).ifPresent(team -> {
        teamOutlineList.add(TeamOutline.of(team));
      });
    }

    return new FindByTeamTitleResponse(teamOutlineList);
  }

}
