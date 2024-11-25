package com.dowe.participation.presentation;

import static com.dowe.util.api.ResponseResult.*;

import com.dowe.participation.application.ParticipationService;
import com.dowe.participation.dto.response.DeleteMyPendingRequestResponse;
import com.dowe.participation.dto.response.GetMyPendingRequestsResponse;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.resolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/participation-request")
public class ParticipationController {

  private final ParticipationService participationService;

  @GetMapping("/me")
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

  @PutMapping("/me/{requestId}/delete")
  public ResponseEntity<ApiResponse<DeleteMyPendingRequestResponse>> deleteMyPendingRequest(
      @Login Long memberId,
      @PathVariable Long requestId
  ) {
    log.info(">>> ParticipationController deleteMyPendingRequest()");

    return ResponseEntity.ok()
        .body(ApiResponse.ok(
            DELETE_PENDING_REQUEST_SUCCESS,
            participationService.deleteMyPendingRequest(
                memberId,
                requestId
            )
        ));
  }

}
