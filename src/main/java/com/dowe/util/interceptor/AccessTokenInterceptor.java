package com.dowe.util.interceptor;

import static com.dowe.auth.TokenType.*;
import static com.dowe.util.AppConstants.*;
import static org.springframework.http.HttpHeaders.*;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dowe.auth.application.TokenManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessTokenInterceptor implements HandlerInterceptor {

	private final TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (CorsUtils.isPreFlightRequest(request)) {
			return true;
		}

		String accessToken = request.getHeader(AUTHORIZATION).substring(BEARER.length());
		Long memberId = tokenManager.parse(accessToken, ACCESS);

		log.info("-- MEMBER ID {} 엑세스 토큰 인터셉터 통과 --", memberId);
		request.setAttribute(MEMBER_ID, memberId);

		return true;
	}

}
