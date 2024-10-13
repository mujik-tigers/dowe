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
      int size,
      String title,
      Long lastUnixTimestamp,
      String lastTieBreakerId
  ) {

    NativeQuery searchQuery = queryBuilder.buildSearchTeamsByTitleQuery(
        size,
        title,
        lastUnixTimestamp,
        lastTieBreakerId
    );

    return elasticsearchOperations.search(searchQuery, TeamDocument.class);
  }

}
