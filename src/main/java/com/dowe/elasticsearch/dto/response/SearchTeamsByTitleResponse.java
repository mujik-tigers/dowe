package com.dowe.elasticsearch.dto.response;

import com.dowe.team.dto.TeamDocumentOutline;
import java.util.List;

public record SearchTeamsByTitleResponse(
    boolean hasMore,
    Long lastUnixTimestamp,
    Long lastTieBreakerId,
    List<TeamDocumentOutline> teamDocumentOutlines
) {

}
