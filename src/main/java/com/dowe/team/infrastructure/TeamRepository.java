package com.dowe.team.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dowe.team.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
