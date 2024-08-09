package com.dowe.profile.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dowe.profile.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
