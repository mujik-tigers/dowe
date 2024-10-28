package com.dowe.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

public class RestDocsConfig {

  @Bean
  public RestDocumentationResultHandler write() {
    return MockMvcRestDocumentation.document(
        "{class-name}/{method-name}",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint())
    );
  }

}
