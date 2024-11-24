package com.dowe.participation.infrastructure;

import com.dowe.participation.Participation;
import com.dowe.participation.ParticipationRequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

  @Query("SELECT p FROM Participation p " +
      "JOIN FETCH p.team t " +
      "WHERE p.member.id = :memberId AND p.status = :status")
  List<Participation> findWithTeamByMemberIdAndStatus(
      @Param("memberId") Long memberId,
      @Param("status") ParticipationRequestStatus status
  );

}
