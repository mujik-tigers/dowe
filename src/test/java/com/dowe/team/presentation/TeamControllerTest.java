package com.dowe.team.presentation;

import static com.dowe.util.AppConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;

import com.dowe.RestDocsSupport;
import com.dowe.team.dto.NewTeam;
import com.dowe.util.api.ResponseResult;

class TeamControllerTest extends RestDocsSupport {

  @Test
  @DisplayName("팀 생성 성공")
  void createSuccess() throws Exception {
    // given
    String authorizationHeader = BEARER + "accessToken";
    NewTeam newTeam = new NewTeam(20L);
    MockMultipartFile image = new MockMultipartFile("image", "test-image.png", "image/png", "some-image".getBytes());

    given(tokenManager.parse(anyString(), any()))
        .willReturn(1L);
    given(teamService.create(anyLong(), any()))
        .willReturn(newTeam);

    // when / then
    mockMvc.perform(multipart("/teams")
            .file(image)
            .param("title", "Sample Team")
            .param("description", "This is a sample description.")
            .header(AUTHORIZATION, authorizationHeader)
            .header(ORIGIN, FRONTEND_ORIGIN)
            .header(HOST, API_HOST))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.getReasonPhrase()))
        .andExpect(jsonPath("$.result").value(ResponseResult.TEAM_CREATE_SUCCESS.getDescription()))
        .andExpect(jsonPath("$.data.teamId").value(newTeam.getTeamId()))
        .andDo(document("team-create-success",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("result").type(JsonFieldType.STRING).description("결과"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.teamId").type(JsonFieldType.NUMBER).description("팀 ID")
            )
        ));
  }

}
