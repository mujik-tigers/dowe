package com.dowe.participation.application;

import static com.dowe.participation.ParticipationRequestStatus.*;

import com.dowe.exception.member.MemberNotFoundException;
import com.dowe.member.infrastructure.MemberRepository;
import com.dowe.participation.Participation;
import com.dowe.participation.dto.response.DeleteMyPendingRequestResponse;
import com.dowe.participation.dto.response.GetMyPendingRequestsResponse;
import com.dowe.participation.infrastructure.ParticipationRepository;
import com.dowe.participation.dto.PendingRequestTeamInfo;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationService {

  private final ParticipationRepository participationRepository;
  private final MemberRepository memberRepository;

  public GetMyPendingRequestsResponse getMyPendingRequests(
      Long memberId
  ) {
    log.info(">>> ParticipationService getMyPendingRequests()");

    memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

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

  @Transactional
  public DeleteMyPendingRequestResponse deleteMyPendingRequest(
      Long memberId,
      Long requestId
  ) {

    log.info(">>> ParticipationService deleteMyPendingRequest()");

    memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    Participation participation = participationRepository.findById(requestId)
        .filter(p -> p.getStatus() == PENDING)
        .orElseThrow(() -> new IllegalArgumentException("참여 요청이 승인, 거절 혹은 삭제되었습니다."));

    if (!participation.getMember().getId().equals(memberId)) {
      throw new SecurityException("가입 요청 삭제는 본인만 할 수 있습니다.");
    }

    participation.deleteRequest();

    return new DeleteMyPendingRequestResponse(participation.getId());
  }

}
