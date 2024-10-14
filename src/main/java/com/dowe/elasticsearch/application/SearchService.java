package com.dowe.elasticsearch.application;

import static com.dowe.util.AppConstants.LAST_TIE_BREAKER_ID_INDEX;
import static com.dowe.util.AppConstants.LAST_UNIX_TIMESTAMP_INDEX;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.elasticsearch.dto.response.SearchTeamsByTitleResponse;
import com.dowe.elasticsearch.infrastructure.ElasticsearchRepository;
import com.dowe.elasticsearch.mapper.TeamMapper;
import com.dowe.team.dto.TeamOutline;
import com.dowe.team.infrastructure.TeamRepository;
import com.dowe.util.AppConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

  private final TeamRepository teamRepository;
  private final ElasticsearchRepository elasticsearchRepository;
  private final TeamMapper teamMapper;

  public SearchTeamsByTitleResponse searchTeamsByTitle(
      String title,
      int requestSize,
      Long lastUnixTimestamp,
      Long lastTieBreakerId
  ) {

    SearchHits<TeamDocument> teamSearchResult = elasticsearchRepository.searchTeamsByTitle(
        title,
        requestSize,
        lastUnixTimestamp,
        lastTieBreakerId
    );

    List<SearchHit<TeamDocument>> teamHits = teamSearchResult.getSearchHits();

    boolean hasMore = teamHits.size() > requestSize;

    List<Object> lastSortValues = getLastSortValues(
        hasMore,
        teamHits
    );

    List<TeamOutline> teamOutlines = convertToTeamOutlines(
        requestSize,
        teamHits
    );

    return new SearchTeamsByTitleResponse(
        hasMore,
        getLastUnixTimestamp(lastSortValues),
        getLastTieBreakerId(lastSortValues),
        teamOutlines
    );
  }

  private List<Object> getLastSortValues(
      boolean hasMore,
      List<SearchHit<TeamDocument>> teamHits
  ) {
    if (hasMore) {
      return teamHits.get(teamHits.size() - 2).getSortValues();
    }
    return teamHits.get(teamHits.size() - 1).getSortValues();
  }

  private List<TeamOutline> convertToTeamOutlines(
      int requestSize,
      List<SearchHit<TeamDocument>> teamHits
  ) {
    return teamHits.stream()
        .map(hit -> teamMapper.toTeamOutline(
                hit.getContent()
            )
        )
        .limit(requestSize)
        .toList();
  }

  private Long getLastUnixTimestamp(
      List<Object> lastSortValues
  ) {
    return (Long) lastSortValues.get(LAST_UNIX_TIMESTAMP_INDEX);
  }

  private Long getLastTieBreakerId(
      List<Object> lastSortValues
  ) {
    return (Long) lastSortValues.get(LAST_TIE_BREAKER_ID_INDEX);
  }

}
