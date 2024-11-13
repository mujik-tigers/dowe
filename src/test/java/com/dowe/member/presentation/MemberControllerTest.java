package com.dowe.member.presentation;

import static com.dowe.TestConstants.AUTHORIZATION;
import static com.dowe.TestConstants.BEARER;
import static com.dowe.TestConstants.TEAM_MAX_SIZE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import com.dowe.RestDocsSupport;
import com.dowe.member.dto.MemberName;
import com.dowe.team.dto.TeamOutline;
import com.dowe.member.dto.response.FetchMyTeamResponse;
import com.dowe.util.api.ResponseResult;

class MemberControllerTest extends RestDocsSupport {

  @Test
  @DisplayName("이름 변경 성공")
  void updateNameSuccess() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";
    MemberName memberName = new MemberName(" your new   name");

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);
    given(memberService.updateName(anyLong(), anyString()))
        .willReturn(new MemberName("your new name"));

    // when / then
    mockMvc.perform(patch("/members/names")
            .header(AUTHORIZATION, authorizationHeader)
            .content(objectMapper.writeValueAsString(memberName))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(
            jsonPath("$.result").value(ResponseResult.MEMBER_NAME_UPDATE_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.newName").value("your new name"))
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.newName").type(JsonFieldType.STRING).description("새로운 이름")
            )
        ));
  }

  @Test
  @DisplayName("이름 변경 실패 : 이름 길이 20자 초과")
  void updateNameFail() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";
    MemberName memberName = new MemberName("your name is too long...");

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);

    // when / then
    mockMvc.perform(patch("/members/names")
            .header(AUTHORIZATION, authorizationHeader)
            .content(objectMapper.writeValueAsString(memberName))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.EXCEPTION_OCCURRED.getDescription()))
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                fieldWithPath("data[].type").type(JsonFieldType.STRING).description("오류 타입"),
                fieldWithPath("data[].message").type(JsonFieldType.STRING).description("오류 메시지")
            )
        ));
  }

  @Test
  @DisplayName("나의 팀 목록 조회 성공")
  void fetchMyTeam() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";

    TeamOutline teamOutline = new TeamOutline(11L,
        "매일 런닝 크루",
        "매일 매일 달려봅시다!",
        "https://source/image.jpg",
        3,
        TEAM_MAX_SIZE
    );

    FetchMyTeamResponse fetchMyTeamResponse = FetchMyTeamResponse.from(List.of(teamOutline));

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);
    given(memberService.fetchMyTeam(anyLong()))
        .willReturn(fetchMyTeamResponse);

    // when / then
    mockMvc.perform(get("/members/teams")
            .header(AUTHORIZATION, authorizationHeader)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.getReasonPhrase()))
        .andExpect(
            jsonPath("$.result").value(ResponseResult.MY_TEAM_LIST_FETCH_SUCCESS.getDescription()))
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.teamOutlines[]").type(JsonFieldType.ARRAY)
                    .description("나의 팀 목록"),
                fieldWithPath("data.teamOutlines[].id").type(JsonFieldType.NUMBER)
                    .description("아이디"),
                fieldWithPath("data.teamOutlines[].title").type(JsonFieldType.STRING)
                    .description("이름"),
                fieldWithPath("data.teamOutlines[].description").type(JsonFieldType.STRING)
                    .description("설명"),
                fieldWithPath("data.teamOutlines[].image").type(JsonFieldType.STRING)
                    .description("프로필 이미지 URL"),
                fieldWithPath("data.teamOutlines[].currentPeople").type(JsonFieldType.NUMBER)
                    .description("현재 인원 수"),
                fieldWithPath("data.teamOutlines[].maxPeople").type(JsonFieldType.NUMBER)
                    .description("최대 인원 수")
            )
        ));
  }

}
