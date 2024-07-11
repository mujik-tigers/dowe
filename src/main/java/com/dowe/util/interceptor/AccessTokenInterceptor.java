package com.dowe.util.interceptor;

import static com.dowe.util.AppConstants.*;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dowe.auth.application.TokenManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessTokenInterceptor implements HandlerInterceptor {

	private final TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String accessToken = authorizationHeader.substring(BEARER.length());
		Long memberId = tokenManager.parse(accessToken);

		request.setAttribute(MEMBER_ID, memberId);

		return true;
	}

}
