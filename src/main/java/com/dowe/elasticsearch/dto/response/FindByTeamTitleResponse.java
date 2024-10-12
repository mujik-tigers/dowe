package com.dowe.elasticsearch.dto.response;

import com.dowe.team.dto.TeamOutline;
import java.util.List;

public record FindByTeamTitleResponse(
    boolean hasMore,
    Long lastUnixTimestamp,
    String lastTieBreakerId,
    List<TeamOutline> teamOutlineList
) {

}
