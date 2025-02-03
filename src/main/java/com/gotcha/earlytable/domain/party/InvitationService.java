package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.notification.FcmService;
import com.gotcha.earlytable.domain.notification.SseEmitterService;
import com.gotcha.earlytable.domain.party.dto.InvitationStatusDto;
import com.gotcha.earlytable.domain.party.dto.ReceivedAllInvitationResponseDto;
import com.gotcha.earlytable.domain.party.dto.ReceivedInvitationResponseDto;
import com.gotcha.earlytable.domain.party.dto.ReceivedWaitingInvitationResponseDto;
import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.reservation.ReservationRepository;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.WaitingRepository;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.enums.NotificationType;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final SseEmitterService sseEmitterService;
    private final FcmService fcmService;
    private final WaitingRepository waitingRepository;

    public InvitationService(final InvitationRepository invitationRepository, UserRepository userRepository, ReservationRepository reservationRepository, PartyPeopleRepository partyPeopleRepository, SseEmitterService sseEmitterService, FcmService fcmService, WaitingRepository waitingRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.sseEmitterService = sseEmitterService;
        this.fcmService = fcmService;
        this.waitingRepository = waitingRepository;
    }


    /**
     *  유저에게 초대 보내기 메서드 (reservation)
     * @param userId
     * @param reservationId
     * @param user
     */
    @Transactional
    public void inviteUserFromReservation(Long userId,Long reservationId, User user){

        User receiveUser = userRepository.findByIdOrElseThrow(userId);

        // reservation에 해당하는 파티가 있고 내가 그 파티의 파티장인가를 검증
        Party party = reservationRepository.findByIdOrElseThrow(reservationId)
                .getParty();
        boolean isRepresent  = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()) && partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE));
        if(!isRepresent){throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);}

        // 이미 일행으로 보낸적이 있는지름 검증
        Invitation invitation = invitationRepository.findBySendUserAndReceiveUserAndParty(user,receiveUser,party);
        if(invitation != null){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);

        }

        // 일행으로 보낸적이 없는경우 null인 invitation에 새로운 invitation의 정보를 넣어 저장
        invitation = new Invitation(user, receiveUser, party);
        invitationRepository.save(invitation);

        // 알림 발송
        sseEmitterService.send(receiveUser, user.getNickName() + "님이 일행으로 초대하였습니다.", NotificationType.PARTY);
        fcmService.sendNotificationByToken("일행 초대", user.getNickName() + "님이 일행으로 초대하였습니다.", "", receiveUser);
    }

    /**
     *  유저에게 초대 보내기 메서드 (waiting)
     * @param userId
     * @param waitingId
     * @param user
     */
    @Transactional
    public void inviteUserFromWaiting(Long userId, Long waitingId, User user) {
        User receiveUser = userRepository.findByIdOrElseThrow(userId);

        // reservation에 해당하는 파티가 있고 내가 그 파티의 파티장인가를 검증
        Party party = waitingRepository.findByIdOrElseThrow(waitingId)
                .getParty();
        boolean isRepresent  = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()) && partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE));
        if(!isRepresent){throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);}

        // 이미 일행으로 보낸적이 있는지름 검증
        Invitation invitation = invitationRepository.findBySendUserAndReceiveUserAndParty(user,receiveUser,party);
        if(invitation != null){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);

        }

        // 일행으로 보낸적이 없는경우 null인 invitation에 새로운 invitation의 정보를 넣어 저장
        invitation = new Invitation(user, receiveUser, party);
        invitationRepository.save(invitation);

        // 알림 발송
        sseEmitterService.send(receiveUser, user.getNickName() + "님이 일행으로 초대하였습니다.", NotificationType.PARTY);
        fcmService.sendNotificationByToken("일행 초대", user.getNickName() + "님이 일행으로 초대하였습니다.", "", receiveUser);
    }


    /**
     *  나에게 온 모든 초대 조회 메서드
     * @param user
     * @return List<ReceivedAllInvitationResponseDto>
     */
    public List<ReceivedAllInvitationResponseDto> getAllReceiveInvitations(User user) {

        // 내가 받은 예약의 리스트를 가져옴
        List<Invitation> invitations = invitationRepository.findByReceiveUser(user);

        // 해당 예약들의 파티가 존재하는지 검사 -> 파티가 없으면 에러를 발생시킴
        boolean isParty = invitations.stream().anyMatch(invitation -> invitation.getParty() == null ||
                (invitation.getParty().getReservation() == null && invitation.getParty().getWaiting() == null));
        if (isParty) {
            throw new CustomException(ErrorCode.NOT_FOUND_PARTY);
        }

        List<ReceivedAllInvitationResponseDto> responseDtos = new ArrayList<>();
        for (Invitation invitation : invitations) {
            // 파티가 존재하는지 확인하고, 어떤 정보를 반환할지 결정
            ReceivedAllInvitationResponseDto dto = null;

            if (invitation.getParty().getReservation() != null) {
                // 예약이 존재하는 경우
                dto = ReceivedAllInvitationResponseDto.toDtoForReservation(invitation);
            } else if (invitation.getParty().getWaiting() != null) {
                // 웨이팅이 존재하는 경우
                dto = ReceivedAllInvitationResponseDto.toDtoForWaiting(invitation);
            }

            responseDtos.add(dto);
        }
        return responseDtos;
    }



    /**
     *  나에게 온 파티 초대 단건 조회 메서드
     * @param invitationId
     * @param user
     * @return ReceivedInvitationResponseDto
     */
    public ReceivedInvitationResponseDto getReceiveInvitation(Long invitationId, User user) {
        // 초대 아이디로 단건의 초대를 가져옴
        Invitation invitation = invitationRepository.findByInvitationIdOrThrow(invitationId);

        // 해당 예약이 내것인지 확인
        if(!invitation.getReceiveUser().getId().equals(user.getId())){throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);}

        // 해당 예약건의 파티가 없는경우
        if(invitation.getParty() == null){throw new CustomException(ErrorCode.NOT_FOUND_PARTY);}

        return ReceivedInvitationResponseDto.toDto(invitation);
    }


    /**
     *  초대 수락/거절 메서드
     * @param invitationId
     * @param requestDto
     * @param user
     * @return
     */
    @Transactional
    public ReceivedInvitationResponseDto changeInvitationStatus(Long invitationId, InvitationStatusDto requestDto, User user) {
        //초대 가져오기
        Invitation invitation = invitationRepository.findByInvitationIdOrThrow(invitationId);

        if(!invitation.getInvitationStatus().equals(InvitationStatus.PENDING)){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 해당 예약이 내것인지 확인
        if(!invitation.getReceiveUser().getId().equals(user.getId())){throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);}

        InvitationStatus status = requestDto.getStatus();

        if(!(status.equals(InvitationStatus.ACCEPTED) || status.equals(InvitationStatus.REJECTED))){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //현재 파티참여 인원수
        Integer countPeople = invitation.getParty().getPartyPeople().size();

        // 수락인 경우 파티피플로 추가 -> 파티 인원수를 넘어서면 안 되므로 체크
        if (status.equals(InvitationStatus.ACCEPTED)) {
            // 예약 상태인지 확인
            if (invitation.getParty().getReservation() != null) {
                if (invitation.getParty().getReservation().getPersonnelCount() <= countPeople) {
                    throw new CustomException(ErrorCode.FULL_PARTY_PEOPLE);
                }
                PartyPeople partyPeople = new PartyPeople(invitation.getParty(), user, PartyRole.REGULAR);
                partyPeopleRepository.save(partyPeople);
            }
            // 웨이팅 상태일 경우 처리 (웨이팅에 추가)
            else if (invitation.getParty().getWaiting() != null) {
                if (invitation.getParty().getWaiting().getPersonnelCount() <= countPeople) {
                    throw new CustomException(ErrorCode.FULL_PARTY_PEOPLE);
                }
                PartyPeople partyPeople = new PartyPeople(invitation.getParty(), user, PartyRole.REGULAR);
                partyPeopleRepository.save(partyPeople);
            }
        }

        invitation.changeStatus(status);
        invitationRepository.save(invitation);


        String message = user.getNickName() + "님이 초대를 " + status.getValue() + "하셨습니다.";
        sseEmitterService.send(invitation.getSendUser(), message, NotificationType.PARTY);

        if (invitation.getParty().getReservation() != null) {
            return ReceivedInvitationResponseDto.toDtoForReservation(invitation);
        } else if (invitation.getParty().getWaiting() != null) {
            return ReceivedInvitationResponseDto.toDtoForWaiting(invitation);
        }
        return ReceivedInvitationResponseDto.toDto(invitation);
    }



}
