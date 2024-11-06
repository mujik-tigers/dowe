package com.dowe.elasticsearch.application;

import static com.dowe.util.AppConstants.HAS_MORE_LAST_HIT_OFFSET;
import static com.dowe.util.AppConstants.NO_MORE_LAST_HIT_OFFSET;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.elasticsearch.dto.response.SearchTeamsByTitleResponse;
import com.dowe.elasticsearch.infrastructure.ElasticsearchRepository;
import com.dowe.elasticsearch.mapper.TeamDocumentMapper;
import com.dowe.team.dto.TeamDocumentOutline;
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

  private final ElasticsearchRepository elasticsearchRepository;
  private final TeamDocumentMapper teamDocumentMapper;

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

    if (teamHits.isEmpty()) {
      return SearchTeamsByTitleResponse.empty();
    }

    boolean hasMore = teamHits.size() > requestSize;

    List<Object> lastSortValues = getLastSortValues(
        hasMore,
        teamHits
    );

    List<TeamDocumentOutline> teamDocumentOutlines = convertToTeamDocumentOutlines(
        requestSize,
        teamHits
    );

    return new SearchTeamsByTitleResponse(
        hasMore,
        getLastUnixTimestamp(lastSortValues),
        getLastTieBreakerId(lastSortValues),
        teamDocumentOutlines
    );
  }

  private List<Object> getLastSortValues(
      boolean hasMore,
      List<SearchHit<TeamDocument>> teamHits
  ) {
    if (hasMore) {
      return teamHits.get(teamHits.size() - HAS_MORE_LAST_HIT_OFFSET).getSortValues();
    }
    return teamHits.get(teamHits.size() - NO_MORE_LAST_HIT_OFFSET).getSortValues();
  }

  private List<TeamDocumentOutline> convertToTeamDocumentOutlines(
      int requestSize,
      List<SearchHit<TeamDocument>> teamHits
  ) {
    return teamHits.stream()
        .map(hit -> teamDocumentMapper.toTeamDocumentOutline(
                hit.getContent()
            )
        )
        .limit(requestSize)
        .toList();
  }

}
