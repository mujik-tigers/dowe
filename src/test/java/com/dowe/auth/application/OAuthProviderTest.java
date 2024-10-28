package com.dowe.auth.application;

import static com.dowe.TestConstants.AUTHORIZATION;
import static com.dowe.TestConstants.BEARER;
import static com.dowe.TestConstants.CONTENT_TYPE;
import static com.dowe.TestConstants.FRONTEND_DOMAIN;
import static com.dowe.TestConstants.X_WWW_FORM_URLENCODED_CHARSET_UTF_8;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import com.dowe.IntegrationTestSupport;
import com.dowe.auth.dto.AccessToken;
import com.dowe.auth.dto.UserResource;
import com.dowe.exception.auth.InvalidAuthorizationCodeException;
import com.dowe.member.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;

class OAuthProviderTest extends IntegrationTestSupport {

  @Autowired
  private ObjectMapper objectMapper;

  private ClientAndServer mockServer;

  @BeforeEach
  public void init() {
    mockServer = ClientAndServer.startClientAndServer(8088);
  }

  @AfterEach
  public void clear() {
    mockServer.stop();
  }

  @ParameterizedTest
  @EnumSource(Provider.class)
  @DisplayName("제대로 된 인증 코드를 받으면 OAuthProvider에 의해 authId를 받는다.")
  void getAuthId(Provider provider) throws Exception {
    // given
    String authorizationCode = "properCode";
    AccessToken accessToken = new AccessToken("properAccessToken");
    UserResource userResource = new UserResource("12312309");

    mockServer
        .when(HttpRequest.request()
            .withMethod("POST")
            .withPath("/oauth/token/" + provider.name())
            .withHeader(CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8))
        .respond(HttpResponse.response()
            .withStatusCode(200)
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(objectMapper.writeValueAsString(accessToken)));

    mockServer
        .when(HttpRequest.request()
            .withMethod("GET")
            .withPath("/oauth/userinfo/" + provider.name())
            .withHeader(CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8)
            .withHeader(AUTHORIZATION, BEARER + accessToken.getAccessToken()))
        .respond(HttpResponse.response()
            .withStatusCode(200)
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(objectMapper.writeValueAsString(userResource)));

    // when
    String authId = authProvider.authenticate(FRONTEND_DOMAIN, provider, authorizationCode);

    // then
    assertThat(authId).isEqualTo(userResource.getId());
  }

  @Test
  @DisplayName("올바르지 않은 인증 코드를 받으면 예외가 발생한다.")
  void getAuthIdFail() throws Exception {
    // given
    String invalidCode = "invalidCode";

    mockServer
        .when(HttpRequest.request()
            .withMethod("POST")
            .withPath("/oauth/token/" + Provider.GOOGLE)
            .withHeader(CONTENT_TYPE, X_WWW_FORM_URLENCODED_CHARSET_UTF_8))
        .respond(HttpResponse.response()
            .withStatusCode(400)
            .withContentType(MediaType.APPLICATION_JSON));

    // when // then
    assertThatThrownBy(() -> authProvider.authenticate(FRONTEND_DOMAIN, Provider.GOOGLE, invalidCode))
        .isInstanceOf(InvalidAuthorizationCodeException.class);

  }

}
