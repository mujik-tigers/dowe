package com.dowe.elasticsearch.infrastructure;

import com.dowe.elasticsearch.document.TeamDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ElasticsearchRepository {

  private final ElasticsearchOperations elasticsearchOperations;
  private final QueryBuilder queryBuilder;

  public SearchHits<TeamDocument> searchTeamsByTitle(
      String title,
      int requestSize,
      Long lastUnixTimestamp,
      Long lastTieBreakerId
  ) {

    NativeQuery searchQuery = queryBuilder.buildSearchTeamsByTitleQuery(
        title,
        requestSize,
        lastUnixTimestamp,
        lastTieBreakerId
    );

    return elasticsearchOperations.search(searchQuery, TeamDocument.class);
  }

}
