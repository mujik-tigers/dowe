package com.dowe.elasticsearch.presentation;

import static com.dowe.TestConstants.BACKEND_DOMAIN;
import static com.dowe.TestConstants.BEARER;
import static com.dowe.TestConstants.AUTHORIZATION;
import static com.dowe.TestConstants.FRONTEND_DOMAIN;
import static com.dowe.TestConstants.HOST;
import static com.dowe.TestConstants.ORIGIN;
import static com.dowe.TestConstants.TEAM_MAX_SIZE;
import static com.dowe.config.RestDocsConfig.field;
import static com.dowe.util.api.ResponseResult.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.dowe.RestDocsSupport;
import com.dowe.elasticsearch.dto.response.SearchTeamsByTitleResponse;
import com.dowe.team.dto.TeamDocumentOutline;

public class SearchControllerTest extends RestDocsSupport {

  @Test
  @DisplayName("팀 제목으로 검색 (최초 시도)")
  void searchTeamsByTitleInitialAttempt() throws Exception {

    // given
    String authorizationHeader = BEARER + "accessToken";

    List<TeamDocumentOutline> teamDocumentOutlines = List.of(
        new TeamDocumentOutline(1L, "Sample Team 1", "Description 1", "image1.jpg", 1, TEAM_MAX_SIZE),
        new TeamDocumentOutline(2L, "Sample Team 2", "Description 2", "image2.jpg", 3, TEAM_MAX_SIZE),
        new TeamDocumentOutline(3L, "Sample Team 3", "Description 3", "image3.jpg", 2, TEAM_MAX_SIZE)
    );

    SearchTeamsByTitleResponse response = new SearchTeamsByTitleResponse(
        true,
        1727936701123L,
        3L,
        teamDocumentOutlines
    );

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);

    given(searchService.searchTeamsByTitle(
        anyString(),
        anyInt(),
        anyLong(),
        anyLong()
    )).willReturn(response);

    // when / then
    mockMvc.perform(get("/search/teams")
            .param("title", "sample")
            .param("size", String.valueOf(3))
            .header(AUTHORIZATION, authorizationHeader)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(TEAM_SEARCH_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.hasMore").value(true))
        .andExpect(jsonPath("$.data.lastUnixTimestamp").value(1727936701123L))
        .andExpect(jsonPath("$.data.lastTieBreakerId").value(3L))
        .andExpect(jsonPath("$.data.teamDocumentOutlines.length()").value(3))
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("title").description("검색 할 팀 제목"),
                parameterWithName("lastUnixTimestamp").attributes(field("default", "0")).description("마지막 검색 결과의 Unix 타임스탬프").optional(),
                parameterWithName("lastTieBreakerId").attributes(field("default", "0")).description("마지막 검색 결과의 Tie Breaker ID").optional(),
                parameterWithName("size").attributes(field("default", "10")).description("요청할 검색 결과의 개수").optional()
            ),
            responseFields(
                fieldWithPath("code").type(NUMBER).description("코드"),
                fieldWithPath("status").type(STRING).description("상태"),
                fieldWithPath("result").type(STRING).description("결과"),
                fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                fieldWithPath("data.hasMore").type(BOOLEAN).description("추가 검색 결과 존재 여부"),
                fieldWithPath("data.lastUnixTimestamp").type(NUMBER).description("마지막 검색 결과의 Unix 타임스탬프"),
                fieldWithPath("data.lastTieBreakerId").type(NUMBER).description("마지막 검색 결과의 Tie Breaker ID"),
                fieldWithPath("data.teamDocumentOutlines").type(ARRAY).description("검색된 팀 목록"),
                fieldWithPath("data.teamDocumentOutlines[].id").type(NUMBER).description("팀 ID"),
                fieldWithPath("data.teamDocumentOutlines[].title").type(STRING).description("팀 제목"),
                fieldWithPath("data.teamDocumentOutlines[].description").type(STRING).description("팀 설명"),
                fieldWithPath("data.teamDocumentOutlines[].image").type(STRING).description("팀 사진"),
                fieldWithPath("data.teamDocumentOutlines[].currentPeople").type(NUMBER).description("팀 현재 인원 수"),
                fieldWithPath("data.teamDocumentOutlines[].maxPeople").type(NUMBER).description("팀 최대 인원 수")
            )
        ));

  }

  @Test
  @DisplayName("팀 제목으로 검색 (후속 시도)")
  void searchTeamsByTitlePostAttempt() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";
    String searchTitle = "sample";
    Long lastUnixTimestamp = 1727936701123L;
    Long lastTieBreakerId = 3L;
    int requestSize = 3;

    List<TeamDocumentOutline> teamDocumentOutlines = List.of(
        new TeamDocumentOutline(4L, "Sample Team 4", "Description 4", "image4.jpg", 7, TEAM_MAX_SIZE),
        new TeamDocumentOutline(5L, "Sample Team 5", "Description 5", "image5.jpg", 4, TEAM_MAX_SIZE),
        new TeamDocumentOutline(6L, "Sample Team 6", "Description 6", "image6.jpg", 9, TEAM_MAX_SIZE)
    );

    SearchTeamsByTitleResponse response = new SearchTeamsByTitleResponse(
        false,
        1727872200462L,
        6L,
        teamDocumentOutlines
    );

    given(searchService.searchTeamsByTitle(
        anyString(),
        anyInt(),
        anyLong(),
        anyLong()
    )).willReturn(response);

    // when / then
    mockMvc.perform(get("/search/teams")
            .param("title", searchTitle)
            .param("size", String.valueOf(requestSize))
            .param("lastUnixTimestamp", String.valueOf(lastUnixTimestamp))
            .param("lastTieBreakerId", String.valueOf(lastTieBreakerId))
            .header(AUTHORIZATION, authorizationHeader)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(TEAM_SEARCH_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.hasMore").value(false))
        .andExpect(jsonPath("$.data.lastUnixTimestamp").value(1727872200462L))
        .andExpect(jsonPath("$.data.lastTieBreakerId").value(6L))
        .andExpect(jsonPath("$.data.teamDocumentOutlines.length()").value(3))
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("title").description("검색 할 팀 제목"),
                parameterWithName("lastUnixTimestamp").attributes(field("default", "-")).description("마지막 검색 결과의 Unix 타임스탬프"),
                parameterWithName("lastTieBreakerId").attributes(field("default", "-")).description("마지막 검색 결과의 Tie Breaker ID"),
                parameterWithName("size").attributes(field("default", "10")).description("요청할 검색 결과의 개수").optional()
            ),
            responseFields(
                fieldWithPath("code").type(NUMBER).description("코드"),
                fieldWithPath("status").type(STRING).description("상태"),
                fieldWithPath("result").type(STRING).description("결과"),
                fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                fieldWithPath("data.hasMore").type(BOOLEAN).description("추가 검색 결과 존재 여부"),
                fieldWithPath("data.lastUnixTimestamp").type(NUMBER).description("마지막 검색 결과의 Unix 타임스탬프"),
                fieldWithPath("data.lastTieBreakerId").type(NUMBER).description("마지막 검색 결과의 Tie Breaker ID"),
                fieldWithPath("data.teamDocumentOutlines").type(ARRAY).description("검색된 팀 목록"),
                fieldWithPath("data.teamDocumentOutlines[].id").type(NUMBER).description("팀 ID"),
                fieldWithPath("data.teamDocumentOutlines[].title").type(STRING).description("팀 제목"),
                fieldWithPath("data.teamDocumentOutlines[].description").type(STRING).description("팀 설명"),
                fieldWithPath("data.teamDocumentOutlines[].image").type(STRING).description("팀 사진"),
                fieldWithPath("data.teamDocumentOutlines[].currentPeople").type(NUMBER).description("팀 현재 인원 수"),
                fieldWithPath("data.teamDocumentOutlines[].maxPeople").type(NUMBER).description("팀 최대 인원 수")
            )
        ));

  }

}
