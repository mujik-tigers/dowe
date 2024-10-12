package com.dowe;

import com.dowe.elasticsearch.application.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

@WebMvcTest(controllers = {
	AuthController.class,
	MemberController.class,
	TeamController.class
})
@AutoConfigureRestDocs
public abstract class RestDocsSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

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

}
