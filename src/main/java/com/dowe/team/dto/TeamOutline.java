package com.dowe.team.dto;

import static com.dowe.util.AppConstants.*;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.team.Team;

public record TeamOutline(
    Long id,
    String title,
    String image,
    int currentPeople,
    int maxPeople
) {

  public static TeamOutline of(
      TeamDocument teamDocument,
      int currentPeople
  ) {
    return new TeamOutline(
        teamDocument.getId(),
        teamDocument.getTitle(),
        teamDocument.getImage(),
        currentPeople,
        TEAM_MAX_SIZE
    );
  }

  public static TeamOutline of(
      Team team
  ) {
    return new TeamOutline(
        team.getId(),
        team.getTitle(),
        team.getImage(),
        team.getProfiles().size(),
        TEAM_MAX_SIZE
    );
  }

}
