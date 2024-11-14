package com.dowe.team.presentation;

import com.dowe.elasticsearch.application.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dowe.team.application.TeamService;
import com.dowe.team.dto.NewTeam;
import com.dowe.team.dto.TeamSettings;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;
import com.dowe.util.resolver.Login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping("/teams")
  public ResponseEntity<ApiResponse<NewTeam>> create(
      @Login Long memberId,
      @ModelAttribute @Valid TeamSettings teamSettings
  ) {
    log.info(">>> TeamController create()");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.created(
                ResponseResult.TEAM_CREATE_SUCCESS,
                teamService.create(memberId, teamSettings)
            )
        );
  }
}
