package com.dowe.team.application;

import static com.dowe.util.AppConstants.*;

import com.dowe.exception.team.TeamCreationLimitException;
import com.dowe.profile.application.ProfileService;
import com.dowe.profile.infrastructure.ProfileRepository;
import com.dowe.team.dto.request.AssignImageRequest;
import com.dowe.team.dto.request.CreateTeamRequest;
import com.dowe.team.dto.response.AssignImageResponse;
import com.dowe.team.dto.response.CreateTeamResponse;
import com.dowe.util.s3.S3PresignedUrlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.member.Member;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.profile.Profile;
import com.dowe.team.Team;
import com.dowe.team.infrastructure.TeamRepository;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;

  private final ProfileService profileService;

  private final S3PresignedUrlGenerator s3PresignedUrlGenerator;
  private final ProfileRepository profileRepository;

  @Transactional
  public CreateTeamResponse create(
      Long memberId,
      CreateTeamRequest request
  ) {

    log.info(">>> TeamService create()");

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    long profileCount = profileService.countProfiles(member.getId());

    if (profileCount == MAXIMUM_TEAM_COUNT) {
      throw new TeamCreationLimitException();
    }

    Team team = Team.builder()
        .title(StringUtil.removeExtraSpaces(request.title()))
        .description(StringUtil.removeExtraSpaces(request.description()))
        .build();

    Profile defaultManagerProfile = profileService.createDefaultProfile(
        team,
        member
    );

    team.assignManagerProfile(defaultManagerProfile);

    Team savedTeam = teamRepository.save(team);
    Long teamId = savedTeam.getId();

    String presignedUrl = s3PresignedUrlGenerator.generatePresignedUrl(
        TEAM_IMAGE_DIRECTORY,
        TEAM_IMAGE_PREFIX,
        teamId
    );

    return new CreateTeamResponse(
        teamId,
        presignedUrl
    );
  }

  @Transactional
  public AssignImageResponse assignImage(
      Long memberId,
      Long teamId,
      AssignImageRequest assignImageRequest
  ) {

    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new IllegalArgumentException("Team not found"));

    if (!isTeamManager(team, memberId)) {
      throw new IllegalArgumentException("Member is not a manager");
    }

    team.assignImage(assignImageRequest.image());

    teamRepository.save(team);

    return new AssignImageResponse(team.getId());

  }

}
