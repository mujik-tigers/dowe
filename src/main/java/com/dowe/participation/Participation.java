package com.dowe.participation;

import static com.dowe.participation.ParticipationRequestStatus.*;

import com.dowe.member.Member;
import com.dowe.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private ParticipationRequestStatus status;

  @Builder
  public Participation(
      Team team,
      Member member,
      ParticipationRequestStatus status
  ) {
    this.team = team;
    this.member = member;
    this.status = status;
  }

  public static Participation of(
      Team team,
      Member member
  ) {
    return Participation.builder()
        .team(team)
        .member(member)
        .status(PENDING)
        .build();
  }

  public void deleteRequest() {
    this.status = DELETED;
  }

}
