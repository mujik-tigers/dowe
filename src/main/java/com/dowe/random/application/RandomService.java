package com.dowe.random.application;

import com.dowe.elasticsearch.mapper.TeamMapper;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.team.infrastructure.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomService {

  private final TeamRepository teamRepository;
  private final MemberRepository memberRepository;
  private final TeamMapper teamMapper;

}
