package com.dowe.auth.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.auth.application.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	private final LoginService loginService;

	@GetMapping("/auth/google")
	public void login(@RequestParam String code) {
		loginService.login(code);
		log.info("ok");
	}

}
