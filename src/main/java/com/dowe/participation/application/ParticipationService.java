package com.dowe.participation.application;

import com.dowe.participation.infrastructure.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationService {

  private final ParticipationRepository participationRepository;

}
