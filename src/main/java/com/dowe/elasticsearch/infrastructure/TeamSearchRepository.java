package com.dowe.elasticsearch.infrastructure;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import com.dowe.elasticsearch.document.TeamDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamSearchRepository {

  private final ElasticsearchOperations elasticsearchOperations;

  public SearchHits<TeamDocument> searchTeamsByTitle(
      String title,
      int size,
      Long lastUnixTimestamp,
      String lastTieBreakerId
  ) {
    NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder()
        .withQuery(new MatchQuery.Builder()
            .field("title")
            .query(title)
            .build()._toQuery())
        .withSort(sort -> sort
            .field(f -> f.field("updated_at").order(SortOrder.Asc)))
        .withSort(sort -> sort
            .field(f -> f.field("tie_breaker_id").order(SortOrder.Asc)))
        .withMaxResults(size + 1);

    if (lastUnixTimestamp != null && lastTieBreakerId != null) {
      nativeQueryBuilder.withSearchAfter(List.of(lastUnixTimestamp, lastTieBreakerId));
    }

    NativeQuery searchQuery = nativeQueryBuilder.build();
    return elasticsearchOperations.search(searchQuery, TeamDocument.class);
  }

}
