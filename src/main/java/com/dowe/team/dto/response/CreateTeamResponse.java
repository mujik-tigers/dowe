package com.dowe.team.dto.response;

public record CreateTeamResponse(
    Long teamId,
    String presignedUrl
) {

}
