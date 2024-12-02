package com.dowe.team.presentation;

import static com.dowe.TestConstants.BEARER;
import static com.dowe.TestConstants.AUTHORIZATION;
import static com.dowe.TestConstants.BACKEND_DOMAIN;
import static com.dowe.TestConstants.FRONTEND_DOMAIN;
import static com.dowe.TestConstants.HOST;
import static com.dowe.TestConstants.ORIGIN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dowe.team.dto.request.CreateTeamRequest;
import com.dowe.team.dto.response.CreateTeamResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import com.dowe.RestDocsSupport;
import com.dowe.util.api.ResponseResult;

class TeamControllerTest extends RestDocsSupport {

  @Test
  @DisplayName("팀 생성 성공")
  void createSuccess() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";

    CreateTeamRequest createTeamRequest = new CreateTeamRequest(
        "Sample Team",
        "Sample Description"
    );

    CreateTeamResponse createTeamResponse = new CreateTeamResponse(
        20L,
        "pre-signed url"
    );

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);
    given(teamService.create(anyLong(), any()))
        .willReturn(createTeamResponse);

    // when / then
    mockMvc.perform(post("/teams")
            .content(objectMapper.writeValueAsString(createTeamRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, authorizationHeader)
            .header(ORIGIN, FRONTEND_DOMAIN)
            .header(HOST, BACKEND_DOMAIN))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.TEAM_CREATE_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.teamId").value(createTeamResponse.teamId()))
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                fieldWithPath("data.presignedUrl").type(JsonFieldType.STRING).description("Pre-signed URL")
            )
        ));
  }

}
