package com.dowe.util.resolver;

import static com.dowe.util.AppConstants.*;

import com.dowe.config.properties.OAuthProperties;
import com.dowe.member.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUriResolver {

  private final OAuthProperties oAuthProperties;

  public String resolvedRedirectUri(
      String origin,
      Provider provider
  ) {

    String baseUri = oAuthProperties.getRedirectBaseUriOf(provider);

    if (origin != null && oAuthProperties.getAllowedOrigins().contains(origin)) {
      return origin + baseUri;
    }
    return FRONTEND_DOMAIN + baseUri;
  }

}
