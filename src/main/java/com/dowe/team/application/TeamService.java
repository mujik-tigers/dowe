package com.dowe.team.application;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

  private static final String TEAM_IMAGE_DIRECTORY = "team";

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final ProfileRepository profileRepository;
  private final S3Uploader s3Uploader;

  @Transactional
  public NewTeam create(Long memberId, TeamSettings teamSettings) {

    log.info(">>> TeamService create()");

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    String image = null;
    if (teamSettings.getImage() != null && !teamSettings.getImage().isEmpty()) {
      image = s3Uploader.upload(TEAM_IMAGE_DIRECTORY, teamSettings.getImage());
    }

    log.info(">>> image: {}", image);

    Team teamBuilder = Team.builder()
        .title(StringUtil.removeExtraSpaces(teamSettings.getTitle()))
        .description(StringUtil.removeExtraSpaces(teamSettings.getDescription()))
        .image(image)
        .manager(member)
        .build();

    log.info(">>> teamBuilder");
    log.info(">>> teamBuilder.getId(): {}", teamBuilder.getId());
    log.info(">>> teamBuilder.getTitle(): {}", teamBuilder.getTitle());
    log.info(">>> teamBuilder.getDescription(): {}", teamBuilder.getDescription());
    log.info(">>> teamBuilder.getImage(): {}", teamBuilder.getImage());
    log.info(">>> teamBuilder.getManager(): {}", teamBuilder.getManager());
    log.info(">>> teamBuilder.getProfiles(): {}", teamBuilder.getProfiles());

    Team team = teamRepository.save(teamBuilder);
    Profile profile = team.join(member);
    profileRepository.save(profile);

    return new NewTeam(team.getId());
  }

}
