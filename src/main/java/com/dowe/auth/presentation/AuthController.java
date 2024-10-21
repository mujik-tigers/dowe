package com.dowe.auth.presentation;

import static com.dowe.util.AppConstants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpHeaders.ORIGIN;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.auth.application.AuthService;
import com.dowe.auth.dto.LoginData;
import com.dowe.auth.dto.TokenPair;
import com.dowe.member.Provider;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/oauth/{provider}")
  public ResponseEntity<ApiResponse<LoginData>> login(
      @PathVariable Provider provider,
      @RequestParam String authorizationCode,
      HttpServletRequest request
  ) {
    String origin = request.getHeader(ORIGIN);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.created(
            ResponseResult.LOGIN_SUCCESS,
            authService.login(
                origin,
                provider,
                authorizationCode
            )
        ));
  }

  @PatchMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenPair>> refreshToken(
      @RequestHeader(AUTHORIZATION) String header) {
    String refreshTokenInput = header.substring(BEARER.length());

    return ResponseEntity.ok()
        .body(ApiResponse.ok(ResponseResult.TOKEN_REFRESH_SUCCESS,
            authService.refresh(refreshTokenInput)));
  }

}
