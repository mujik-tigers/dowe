package com.dowe.team.application;

import static com.dowe.util.AppConstants.*;

import com.dowe.exception.team.TeamCreationLimitException;
import com.dowe.profile.application.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.member.Member;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.profile.Profile;
import com.dowe.team.Team;
import com.dowe.team.dto.NewTeam;
import com.dowe.team.dto.TeamSettings;
import com.dowe.team.infrastructure.TeamRepository;
import com.dowe.util.S3Uploader;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

  private static final String TEAM_IMAGE_DIRECTORY = "team";

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final S3Uploader s3Uploader;

  private final ProfileService profileService;

  @Transactional
  public NewTeam create(Long memberId, TeamSettings teamSettings) {

    log.info(">>> TeamService create()");

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    long profileCount = profileService.countProfiles(member.getId());
    log.info(">>> ProfileService create() profileCount for member {} : {}", memberId, profileCount);

    if (profileCount == MAXIMUM_TEAM_COUNT) {
      throw new TeamCreationLimitException();
    }

    String image = null;
    if (teamSettings.getImage() != null && !teamSettings.getImage().isEmpty()) {
      image = s3Uploader.upload(TEAM_IMAGE_DIRECTORY, teamSettings.getImage());
    }

    log.info(">>> image: {}", image);

    Team team = Team.builder()
        .title(StringUtil.removeExtraSpaces(teamSettings.getTitle()))
        .description(StringUtil.removeExtraSpaces(teamSettings.getDescription()))
        .image(image)
        .build();

    Profile defaultManagerProfile = profileService.createDefaultProfile(
        team,
        member
    );

    team.assignManagerProfile(defaultManagerProfile);

    teamRepository.save(team);

    return new NewTeam(team.getId());
  }
}
