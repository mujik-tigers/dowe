package com.dowe.elasticsearch.mapper;

import static com.dowe.util.AppConstants.*;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.team.dto.TeamOutline;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

  public TeamOutline toTeamOutline(
      TeamDocument teamDocument
  ) {
    return new TeamOutline(
        teamDocument.getId(),
        teamDocument.getTitle(),
        teamDocument.getDescription(),
        teamDocument.getMemberCount(),
        TEAM_MAX_SIZE
    );
  }

}
