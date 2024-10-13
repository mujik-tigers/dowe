package com.dowe.elasticsearch.presentation;

import com.dowe.elasticsearch.application.SearchService;
import com.dowe.elasticsearch.dto.response.SearchByTeamTitleResponse;
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
  public ResponseEntity<ApiResponse<SearchByTeamTitleResponse>> searchTeamsByTitle(
      @RequestParam(required = true) String title,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "0") Long lastUnixTimestamp,
      @RequestParam(defaultValue = "0") String lastTieBreakerId
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.ok(
                ResponseResult.TEAM_FIND_SUCCESS,
                searchService.searchByTeamTitle(
                    size,
                    title,
                    lastUnixTimestamp,
                    lastTieBreakerId
                )
            )
        );
  }

}
