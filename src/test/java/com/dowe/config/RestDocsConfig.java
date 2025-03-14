package com.dowe.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.snippet.Attributes.Attribute;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

@TestConfiguration
public class RestDocsConfig {

  @Bean
  public RestDocumentationResultHandler write() {
    return MockMvcRestDocumentation.document(
        "{class-name}/{method-name}",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint())
    );
  }

  public static final Attribute field(
      final String key,
      final String value
  ) {
    return new Attribute(key, value);
  }

}
