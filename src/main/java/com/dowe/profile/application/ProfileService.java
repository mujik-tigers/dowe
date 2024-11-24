package com.dowe.profile.application;

import com.dowe.member.Member;
import com.dowe.profile.Profile;
import com.dowe.profile.infrastructure.ProfileRepository;
import com.dowe.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;

  @Transactional
  public Profile createDefaultProfile(
      Team team,
      Member member
  ) {

    String nickname = member.getName();

    Profile defaultProfile = Profile.of(
        team,
        member,
        null,
        nickname,
        null
    );

    return profileRepository.save(defaultProfile);
  }

}
