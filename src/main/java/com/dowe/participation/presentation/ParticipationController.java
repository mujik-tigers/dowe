package com.dowe.participation.presentation;

import static com.dowe.util.api.ResponseResult.*;

import com.dowe.participation.application.ParticipationService;
import com.dowe.participation.dto.response.GetMyPendingRequestsResponse;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.resolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParticipationController {

  private final ParticipationService participationService;

  @GetMapping("/participation-request/me")
  public ResponseEntity<ApiResponse<GetMyPendingRequestsResponse>> getMyPendingRequests(
      @Login Long memberId
  ) {
    log.info(">>> ParticipationController getMyPendingRequests()");
    return ResponseEntity.ok()
        .body(ApiResponse.ok(
            MY_PENDING_REQUESTS_SUCCESS,
            participationService.getMyPendingRequests(memberId)
        ));
  }

}
