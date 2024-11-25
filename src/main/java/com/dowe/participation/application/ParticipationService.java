package com.dowe.participation.application;

import static com.dowe.participation.ParticipationRequestStatus.*;

import com.dowe.participation.Participation;
import com.dowe.participation.dto.response.GetMyPendingRequestsResponse;
import com.dowe.participation.infrastructure.ParticipationRepository;
import com.dowe.participation.dto.PendingRequestTeamInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationService {

  private final ParticipationRepository participationRepository;

  public GetMyPendingRequestsResponse getMyPendingRequests(
      Long memberId
  ) {
    log.info(">>> ParticipationService getMyPendingRequests()");

    List<Participation> myPendingRequests = participationRepository.findWithTeamByMemberIdAndStatus(
        memberId,
        PENDING
    );

    List<PendingRequestTeamInfo> myPendingRequestsTeamInfos = myPendingRequests.stream()
        .map(participation -> new PendingRequestTeamInfo(
            participation.getId(),
            participation.getTeam().getTitle(),
            participation.getTeam().getImage()
        ))
        .toList();

    return new GetMyPendingRequestsResponse(myPendingRequestsTeamInfos);
  }

}
