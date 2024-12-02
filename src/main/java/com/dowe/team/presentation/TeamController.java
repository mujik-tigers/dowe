package com.dowe.team.presentation;

import com.dowe.team.dto.request.AssignImageRequest;
import com.dowe.team.dto.request.CreateTeamRequest;
import com.dowe.team.dto.response.AssignImageResponse;
import com.dowe.team.dto.response.CreateTeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.team.application.TeamService;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;
import com.dowe.util.resolver.Login;

import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/teams")
  public ResponseEntity<ApiResponse<CreateTeamResponse>> create(
      @Login Long memberId,
      @RequestBody CreateTeamRequest createTeamRequest
  ) {
    log.info(">>> TeamController create()");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.created(
                ResponseResult.TEAM_CREATE_SUCCESS,
                teamService.create(memberId, createTeamRequest)
            )
        );
  }

  @PutMapping("/teams/{teamId}/image")
  public ResponseEntity<ApiResponse<AssignImageResponse>> assignImage(
      @Login Long memberId,
      @PathVariable Long teamId,
      @RequestBody AssignImageRequest assignImageRequest
  ) {
    log.info(">>> TeamController assignImage()");
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.ok(
            ResponseResult.TEAM_IMAGE_ASSIGN_SUCCESS,
            teamService.assignImage(memberId, teamId, assignImageRequest)
        ));
  }

}
