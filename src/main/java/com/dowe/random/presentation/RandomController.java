package com.dowe.random.presentation;

import static com.dowe.util.api.ResponseResult.RANDOM_TEAMS_SUCCESS;

import com.dowe.random.application.RandomService;
import com.dowe.random.dto.response.RandomTeamsResponse;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/random")
public class RandomController {

  private final RandomService randomService;

  @GetMapping("/teams")
  public ResponseEntity<ApiResponse<RandomTeamsResponse>> getRandomTeams(
      @Login Long memberId
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.ok(
                RANDOM_TEAMS_SUCCESS,
                randomService.getRandomTeams(memberId)
            )
        );
  }

}
