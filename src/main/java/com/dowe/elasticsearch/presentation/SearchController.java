package com.dowe.elasticsearch.presentation;

import com.dowe.elasticsearch.application.SearchService;
import com.dowe.elasticsearch.dto.response.SearchTeamsByTitleResponse;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/teams")
  public ResponseEntity<ApiResponse<SearchTeamsByTitleResponse>> searchTeamsByTitle(
      @RequestParam(required = true) String title,
      @RequestParam(defaultValue = "0") Long lastUnixTimestamp,
      @RequestParam(defaultValue = "0") Long lastTieBreakerId,
      @RequestParam(name = "size", defaultValue = "10") int requestSize
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.ok(
            ResponseResult.TEAM_FIND_SUCCESS,
            searchService.searchTeamsByTitle(
                title,
                requestSize,
                lastUnixTimestamp,
                lastTieBreakerId
            )
        ));
  }

}
