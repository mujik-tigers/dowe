package com.dowe.profile.application;

import com.dowe.profile.infrastructure.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;

}
