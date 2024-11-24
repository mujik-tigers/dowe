package com.dowe.team.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dowe.team.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

  @Query("SELECT p.team FROM Profile p " +
      "WHERE p.member.id = :memberId")
  List<Team> findAllTeamsByMemberId(Long memberId);

  @Query("SELECT COUNT(p) FROM Profile p " +
      "WHERE p.team.id = :teamId")
  int countMembersByTeamId(Long teamId);

  @Query(value = "SELECT * FROM team t " +
      "WHERE t.id NOT IN (" +
      "   SELECT p.team_id FROM profile p " +
      "   WHERE p.member_id = :memberId" +
      ") " +
      "LIMIT 20 OFFSET :offset", nativeQuery = true)
  List<Team> findTeamsNotJoinedByMemberWithOffset(Long memberId, int offset);

  @Query(value = "SELECT COUNT(*) FROM team t " +
      "WHERE t.id NOT IN (" +
      "   SELECT p.team_id FROM profile p " +
      "   WHERE p.member_id = :memberId" +
      ")",
      nativeQuery = true)
  int countTeamsNotJoinedByMember(Long memberId);

}
