package com.dowe.participation.dto.response;

import com.dowe.participation.dto.PendingRequestTeamInfo;
import java.util.List;

public record GetMyPendingRequestsResponse(
    List<PendingRequestTeamInfo> pendingRequestTeamInfos
) {

}
