package com.dowe.elasticsearch.application;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.elasticsearch.dto.response.FindByTeamTitleResponse;
import com.dowe.elasticsearch.infrastructure.TeamSearchRepository;
import com.dowe.elasticsearch.mapper.TeamMapper;
import com.dowe.team.dto.TeamOutline;
import com.dowe.team.infrastructure.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final TeamRepository teamRepository;
  private final TeamSearchRepository teamSearchRepository;
  private final TeamMapper teamMapper;

  public FindByTeamTitleResponse findByTeamTitle(
      int size,
      String teamTitle,
      long lastUnixTimestamp,
      String lastTieBreakerId
  ) {

    List<SearchHit<TeamDocument>> searchHits = teamSearchRepository.searchTeamsByTitle(
        teamTitle,
        size,
        lastUnixTimestamp,
        lastTieBreakerId
    ).getSearchHits();

    int searchHitsSize = searchHits.size();
    boolean hasMore = searchHitsSize > size;
    if (hasMore) {
      searchHitsSize--;
    }

    List<Object> sortValues = searchHits.get(searchHitsSize - 1).getSortValues();
    long nextUnixTimestamp = (long) sortValues.get(0);
    String nextTieBreakerId = (String) sortValues.get(1);

    List<TeamOutline> teamOutlineList = searchHits.stream()
        .map(hit -> {
          TeamDocument teamDocument = hit.getContent();
          int currentPeople = teamRepository.countMembersByTeamId(teamDocument.getId());
          return teamMapper.toTeamOutline(
              teamDocument,
              currentPeople
          );
        }).toList();

    return new FindByTeamTitleResponse(
        hasMore,
        nextUnixTimestamp,
        nextTieBreakerId,
        teamOutlineList
    );
  }

}
