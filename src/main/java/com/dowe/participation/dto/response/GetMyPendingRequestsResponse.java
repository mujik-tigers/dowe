package com.dowe.participation.dto.response;

import com.dowe.team.dto.PendingRequestTeamInfo;
import java.util.List;

public record GetMyPendingRequestsResponse(
    List<PendingRequestTeamInfo> pendingRequestTeamInfos
) {

}
