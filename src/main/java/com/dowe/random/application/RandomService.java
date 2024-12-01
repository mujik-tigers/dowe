package com.dowe.random.application;

import static com.dowe.util.AppConstants.*;

import com.dowe.elasticsearch.mapper.TeamMapper;
import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.member.Member;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.profile.infrastructure.ProfileRepository;
import com.dowe.random.dto.response.RandomTeamsResponse;
import com.dowe.team.dto.TeamOutline;
import com.dowe.team.infrastructure.TeamRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomService {

  private final ProfileRepository profileRepository;
  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamMapper teamMapper;

  public RandomTeamsResponse getRandomTeams(
      Long memberId
  ) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    int memberNotJoinedTeamsCount = teamRepository.countTeamsNotJoinedByMember(member.getId());

    int maxOffset = Math.max(0, memberNotJoinedTeamsCount - RANDOM_TEAMS_SIZE);

    int randomOffset = new Random().nextInt(maxOffset + 1);

    List<TeamOutline> randomTeamOutlines = teamRepository.findTeamsNotJoinedByMemberWithOffset(memberId, randomOffset)
        .stream()
        .map(team -> {
          int currentPeople = profileRepository.countByTeamId(team.getId());
          return teamMapper.toTeamOutline(team, currentPeople);
        })
        .toList();

    return RandomTeamsResponse.from(randomTeamOutlines);

  }

}
