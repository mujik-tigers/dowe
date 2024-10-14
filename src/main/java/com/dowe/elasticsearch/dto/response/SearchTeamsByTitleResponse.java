package com.dowe.elasticsearch.dto.response;

import com.dowe.team.dto.TeamOutline;
import java.util.List;

public record SearchTeamsByTitleResponse(
    boolean hasMore,
    Long lastUnixTimestamp,
    List<TeamOutline> teamOutlineList
    Long lastTieBreakerId,
) {

}
