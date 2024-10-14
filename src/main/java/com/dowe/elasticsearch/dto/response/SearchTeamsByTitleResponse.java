package com.dowe.elasticsearch.dto.response;

import com.dowe.team.dto.TeamOutline;
import java.util.List;

public record SearchTeamsByTitleResponse(
    boolean hasMore,
    Long lastUnixTimestamp,
    Long lastTieBreakerId,
    List<TeamOutline> teamOutlines
) {

}
