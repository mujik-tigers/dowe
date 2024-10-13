package com.dowe.elasticsearch.infrastructure;

import static co.elastic.clients.elasticsearch._types.SortOrder.*;
import static com.dowe.util.AppConstants.*;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery.Builder;
import java.util.List;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueryBuilder {

  public NativeQuery buildSearchTeamsByTitleQuery(
      int size,
      String title,
      Long lastUnixTimeStamp,
      String lastTieBreakerId
  ) {
    NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder()
        .withQuery(new Builder()
            .field(TEAM_TITLE)
            .query(title)
            .build()._toQuery())
        .withSort(sort -> sort
            .field(f -> f.field(UPDATED_AT).order(Asc)))
        .withSort(sort -> sort
            .field(f -> f.field(TIE_BREAKER_ID).order(Asc)))
        .withMaxResults(size + 1);

    if (lastUnixTimeStamp != null && lastTieBreakerId != null) {
      nativeQueryBuilder.withSearchAfter(List.of(lastUnixTimeStamp, lastTieBreakerId));
    }

    return nativeQueryBuilder.build();
  }

}
