package com.dowe.participation.infrastructure;

import com.dowe.participation.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

}
