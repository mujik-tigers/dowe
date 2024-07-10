package com.dowe.util.interceptor;

import static com.dowe.util.AppConstants.*;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dowe.auth.application.TokenManager;
import com.dowe.exception.auth.InvalidAuthorizationHeaderException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessTokenInterceptor implements HandlerInterceptor {

	private final TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (CorsUtils.isPreFlightRequest(request))
			return true;

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
			throw new InvalidAuthorizationHeaderException();
		}

		String accessToken = authorizationHeader.substring(BEARER.length());
		Long memberId = tokenManager.parse(accessToken);

		request.setAttribute(MEMBER_ID, memberId);

		return true;
	}

}
