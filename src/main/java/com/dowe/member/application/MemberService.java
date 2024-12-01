package com.dowe.member.application;

import static com.dowe.util.RandomUtil.*;

import com.dowe.elasticsearch.mapper.TeamMapper;
import com.dowe.profile.infrastructure.ProfileRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.exception.member.MemberRegisterException;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.dto.MemberName;
import com.dowe.team.dto.TeamOutline;
import com.dowe.member.dto.response.FetchMyTeamResponse;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.team.infrastructure.TeamRepository;
import com.dowe.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final TeamMapper teamMapper;

  private final ProfileRepository profileRepository;
  private final MemberRepository memberRepository;
  private final MemberCodeStorage memberCodeStorage;
  private final TeamRepository teamRepository;

  public Optional<Member> findBy(Provider provider, String authId) {
    return memberRepository.findByProvider(provider, authId);
  }

  @Transactional
  public Member register(Provider provider, String authId) {
    Member member = Member.builder()
        .provider(provider)
        .authId(authId)
        .name(generateMemberName())
        .code(generateUniqueMemberCode(provider))
        .build();

    try {
      return memberRepository.save(member);
    } catch (DataIntegrityViolationException exception) {
      throw new MemberRegisterException(provider, authId);
    }
  }

  private String generateUniqueMemberCode(Provider provider) {
    String code = generateMemberCode(provider);
    while (!memberCodeStorage.saveMemberCodeIfUnique(code)) {
      code = generateMemberCode(provider);
    }

    return code;
  }

  @Transactional
  public MemberName updateName(Long memberId, String newName) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    newName = StringUtil.removeExtraSpaces(newName);
    member.updateName(newName);

    return new MemberName(newName);
  }

  public FetchMyTeamResponse fetchMyTeam(
      Long memberId
  ) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

    List<TeamOutline> teamOutlines = teamRepository.findAllTeamsByMemberId(member.getId())
        .stream()
        .map(team -> {
          int currentPeople = profileRepository.countByTeamId(team.getId());
          return teamMapper.toTeamOutline(team, currentPeople);
        })
        .toList();

    return FetchMyTeamResponse.from(teamOutlines);
  }

}
