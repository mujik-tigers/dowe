package com.dowe.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.auth.application.AuthService;
import com.dowe.auth.dto.LoginMember;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/oauth/kakao")
	public ResponseEntity<ApiResponse<LoginMember>> login(@RequestParam String authorizationCode) {
		return ResponseEntity.ok()
			.body(ApiResponse.ok(ResponseResult.EXCEPTION_OCCURRED, authService.loginByKakao(authorizationCode)));
	}

}
