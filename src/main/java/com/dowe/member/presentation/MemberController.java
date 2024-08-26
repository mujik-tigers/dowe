package com.dowe.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.member.application.MemberService;
import com.dowe.member.dto.MemberName;
import com.dowe.member.dto.MyTeamList;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;
import com.dowe.util.resolver.Login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PatchMapping("/members/names")
	public ResponseEntity<ApiResponse<MemberName>> updateName(@Login Long memberId, @RequestBody @Valid MemberName memberName) {
		return ResponseEntity.ok()
			.body(ApiResponse.ok(ResponseResult.MEMBER_NAME_UPDATE_SUCCESS, memberService.updateName(memberId, memberName.getNewName())));
	}

	@GetMapping("/members/teams")
	public ResponseEntity<ApiResponse<MyTeamList>> fetchMyTeam(@Login Long memberId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.ok(ResponseResult.MY_TEAM_FETCH_SUCCESS, memberService.fetchMyTeam(memberId)));
	}

}
