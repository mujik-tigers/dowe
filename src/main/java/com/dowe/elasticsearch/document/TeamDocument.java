package com.dowe.elasticsearch.document;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Document(indexName = "team")
public class TeamDocument {

  @Id
  private Long id;
  private String title;
  private String description;
  private String image;

}
