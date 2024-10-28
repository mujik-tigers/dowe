package com.dowe;

import com.dowe.config.RestDocsConfig;
import com.dowe.elasticsearch.application.SearchService;
import com.dowe.elasticsearch.presentation.SearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

import com.dowe.auth.application.AuthService;
import com.dowe.auth.application.TokenManager;
import com.dowe.auth.presentation.AuthController;
import com.dowe.member.application.MemberService;
import com.dowe.member.presentation.MemberController;
import com.dowe.team.application.TeamService;
import com.dowe.team.presentation.TeamController;
import com.dowe.util.interceptor.AccessTokenInterceptor;
import com.dowe.util.interceptor.AuthorizationHeaderInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(controllers = {
    AuthController.class,
    MemberController.class,
    TeamController.class,
    SearchController.class
})
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected RestDocumentationResultHandler restDocs;

  @SpyBean
  protected AccessTokenInterceptor accessTokenInterceptor;

  @SpyBean
  protected AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

  @MockBean
  protected AuthService authService;

  @MockBean
  protected TokenManager tokenManager;

  @MockBean
  protected MemberService memberService;

  @MockBean
  protected TeamService teamService;

  @MockBean
  protected SearchService searchService;

  @BeforeEach
  void setUp(
      final WebApplicationContext context,
      final RestDocumentationContextProvider provider
  ) {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restDocs)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .build();
  }


}
