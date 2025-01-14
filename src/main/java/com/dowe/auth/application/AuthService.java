package com.dowe.auth.application;

import static com.dowe.auth.TokenType.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dowe.auth.dto.LoginData;
import com.dowe.auth.dto.TokenPair;
import com.dowe.member.Member;
import com.dowe.member.Provider;
import com.dowe.member.application.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final OAuthProvider authProvider;
  private final MemberService memberService;
  private final TokenManager tokenManager;

  public LoginData login(
      String origin,
      Provider provider,
      String authorizationCode
  ) {
    String authId = authProvider.authenticate(
        origin,
        provider,
        authorizationCode
    );

    return generateLoginData(provider, authId);
  }

  public LoginData generateLoginData(Provider provider, String authId) {
    return memberService.findBy(provider, authId)
        .map(member -> LoginData.from(member, tokenManager.issue(member.getId()), false))
        .orElseGet(() -> {
          Member member = memberService.register(provider, authId);
          return LoginData.from(member, tokenManager.issue(member.getId()), true);
        });
  }

  public TokenPair refresh(String refreshTokenInput) {
    Long memberId = tokenManager.parse(refreshTokenInput, REFRESH);
    return tokenManager.refresh(memberId, refreshTokenInput);
  }

}
