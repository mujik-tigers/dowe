package com.dowe.elasticsearch.dto.response;

import static com.dowe.util.AppConstants.LAST_TIE_BREAKER_ID_INDEX;
import static com.dowe.util.AppConstants.LAST_UNIX_TIMESTAMP_INDEX;

import com.dowe.team.dto.TeamDocumentOutline;
import java.util.List;

public record SearchTeamsByTitleResponse(
    boolean hasMore,
    Long lastUnixTimestamp,
    Long lastTieBreakerId,
    List<TeamDocumentOutline> teamDocumentOutlines
) {

  public static SearchTeamsByTitleResponse empty() {
    return new SearchTeamsByTitleResponse(
        false,
        0L,
        0L,
        List.of()
    );
  }

  public static SearchTeamsByTitleResponse of(
      boolean hasMore,
      List<Object> lastSortValues,
      List<TeamDocumentOutline> teamDocumentOutlines
  ) {
    return new SearchTeamsByTitleResponse(
        hasMore,
        (Long) lastSortValues.get(LAST_UNIX_TIMESTAMP_INDEX),
        (Long) lastSortValues.get(LAST_TIE_BREAKER_ID_INDEX),
        teamDocumentOutlines
    );
  }

}
