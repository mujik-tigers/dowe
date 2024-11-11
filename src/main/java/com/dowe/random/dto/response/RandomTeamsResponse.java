package com.dowe.random.dto.response;

import com.dowe.team.dto.TeamOutline;
import java.util.List;

public record RandomTeamsResponse(
    List<TeamOutline> randomTeamOutlines
) {

  public static RandomTeamsResponse from(
      List<TeamOutline> randomTeamOutlines
  ) {
    return new RandomTeamsResponse(
        randomTeamOutlines
    );
  }


}
