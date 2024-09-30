package com.dowe.elasticsearch.infrastructure;

import com.dowe.elasticsearch.document.TeamDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TeamDocumentRepository extends ElasticsearchRepository<TeamDocument, Long> {

}
