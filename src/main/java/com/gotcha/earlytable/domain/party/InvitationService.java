package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.dto.InvitationStatusDto;
import com.gotcha.earlytable.domain.party.dto.ReceivedInvitationResponseDto;
import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.reservation.ReservationRepository;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final PartyRepository partyRepository;

    public InvitationService(final InvitationRepository invitationRepository, UserRepository userRepository, ReservationRepository reservationRepository, PartyPeopleRepository partyPeopleRepository , PartyRepository partyRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.partyRepository = partyRepository;
    }


    /**
     *  유저에게 초대 보내기 메서드
     * @param userId
     * @param reservationId
     * @param user
     */
    @Transactional
    public void inviteUser(Long userId,Long reservationId, User user){
        User receiveUser = userRepository.findByIdOrElseThrow(userId);

        // userId => 예약을 받게될 유저,  reservationId => 파티의 주체가 되는 예약
        boolean isUser = userRepository.existsById(userId);
        if(!isUser){throw new CustomException(ErrorCode.NOT_FOUND); }

        // reservation에 해당하는 파티가 있고 내가 그 파티의 파티장인가를 검증
        Party party = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND))
                .getParty();
        boolean isRepresent  = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getUser().getId().equals(userId) && partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE));
        if(!isRepresent){throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);}

        boolean isReservation = reservationRepository.existsById(reservationId);
        if(!isReservation){throw new CustomException(ErrorCode.NOT_FOUND);}

        // 이미 일행으로 보낸적이 있는지름 검증
        Invitation invitation = invitationRepository.findBySendUserAndReceiveUserAndParty(user,receiveUser,party);
        if(invitation != null){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);

        }

        // 일행으로 보낸적이 없는경우 null인 invitation에 새로운 invitation의 정보를 넣어 저장
        invitation = new Invitation(user, receiveUser, party);
        invitationRepository.save(invitation);

    }


    /**
     *  나에게 온 모든 초대 조회 메서드
     * @param user
     * @return
     */
    public List<ReceivedInvitationResponseDto> getAllReceiveInvitations(User user) {

        // 내가 받은 예약의 리스트를 가져옴
        List<Invitation> invitations = invitationRepository.findByReceiveUser(user);

        // 해당 예약들의 파티가 존재하는지를 검사  -> 파티가 존재한다면 정상적으로 예약이 존재하기에 별다른 검사를 진행X
        boolean isParty = invitations.stream().anyMatch(invitation -> invitation.getParty() == null);
        if(isParty){throw new CustomException(ErrorCode.NOT_FOUND_PARTY);}

        List<ReceivedInvitationResponseDto> responseDtos = new ArrayList<>();
        for(Invitation invitation : invitations){
            String storeName = invitation.getParty().getReservation().getStore().getStoreName();
            User sendUser =  invitation.getSendUser();
            LocalDateTime reservationTime = invitation.getParty().getReservation().getReservationDate().atTime(invitation.getParty().getReservation().getReservationTime());
            Integer personnelCount = invitation.getParty().getReservation().getPersonnelCount();
            InvitationStatus status = invitation.getInvitationStatus();

            ReceivedInvitationResponseDto dto = new ReceivedInvitationResponseDto(storeName, sendUser, reservationTime, personnelCount, status);

            responseDtos.add(dto);
        }
        return responseDtos;
    }

    /**
     *  나에게 온 예약 단건 조회 메서드
     * @param invitationId
     * @param user
     * @return ReceivedInvitationResponseDto
     */
    public ReceivedInvitationResponseDto getReceiveInvitation(Long invitationId, User user) {
        // 초대 아이디로 단건의 초대를 가져옴
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 해당 예약이 내것인지 확인
        if(!invitation.getReceiveUser().equals(user)){throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);}

        // 해당 예약건의 파티가 없는경우
        if(invitation.getParty() == null){throw new CustomException(ErrorCode.NOT_FOUND_PARTY);}

        return new ReceivedInvitationResponseDto(invitation.getParty().getReservation().getStore().getStoreName(),invitation.getSendUser(),
                invitation.getParty().getReservation().getReservationDate().atTime(invitation.getParty().getReservation().getReservationTime()),
                invitation.getParty().getReservation().getPersonnelCount(),invitation.getInvitationStatus());
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
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 해당 예약이 내것인지 확인
        if(!invitation.getReceiveUser().equals(user)){throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);}

        InvitationStatus status = requestDto.getStatus();

        if(!(status.equals(InvitationStatus.ACCEPTED) || status.equals(InvitationStatus.REJECTED))){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 수락인 경우 파티피플로 추가
        if(status.equals(InvitationStatus.ACCEPTED)){
            PartyPeople partyPeople = new PartyPeople(invitation.getParty(), user, PartyRole.REPRESENTATIVE);
            partyPeopleRepository.save(partyPeople);
        }

        invitation.changeStatus(status);
        invitationRepository.save(invitation);

        return new ReceivedInvitationResponseDto(invitation.getParty().getReservation().getStore().getStoreName(),invitation.getSendUser(),
                invitation.getParty().getReservation().getReservationDate().atTime(invitation.getParty().getReservation().getReservationTime()),
                invitation.getParty().getReservation().getPersonnelCount(),invitation.getInvitationStatus());
    }

    /**
     *  파티 탈퇴 메서드
     * @param invitationId
     * @param user
     */
    @Transactional
    public void leaveInvitation(Long invitationId, User user) {
        //초대 가져오기
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 예약을 초대로 받은 기억이 있는지 확인
        if(!invitation.getReceiveUser().equals(user)){throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);}

        // 해당 예약의 상태가 수락인지 확인
        if(!invitation.getInvitationStatus().equals(InvitationStatus.ACCEPTED)){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //초대에서 상태 바꾸기
        invitation.changeStatus(InvitationStatus.LEAVED);
        invitationRepository.save(invitation);

        // 탈퇴하면 파티피플에서 제외
        partyPeopleRepository.deleteByUser(user);



    }

    /**
     *  파티원 1명 추방 메서드
     * @param partyId
     * @param userId
     * @param user
     */
    @Transactional
    public void exilePartyPeople(Long partyId, Long userId, User user) {

        // 파티를 가져와
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 일단 초대장 정보도 가져와
        Invitation invitation = (Invitation) invitationRepository.findByParty(party).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 해당 유저가 파티원으로 존재하는가?
        boolean isPartyPeople = invitation.getParty().getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REGULAR) && partyPeople.getUser().getId().equals(userId));
        if(!isPartyPeople){throw new CustomException(ErrorCode.BAD_REQUEST);}

        // 내가 해당 파티의 파티장이 맞는가?
        boolean isPartyLeader = invitation.getParty().getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE) && partyPeople.getUser().equals(user));
        if(!isPartyLeader){throw new CustomException(ErrorCode.FORBIDDEN_PARTY_LEADER);}

        // 파티피플에서 추방 후 초대장 정보를 떠남으로 변경
        partyPeopleRepository.deleteByUserId(userId);
        invitation.changeStatus(InvitationStatus.EXILE);
        invitationRepository.save(invitation);

    }

    /**
     *  파티원 전체 추방 메서드
     * @param partyId
     * @param user
     */
    @Transactional
    public void exileAllPartyPeople(Long partyId, User user) {

        // 해당 파티 정보를 가져오기
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        //해당 파티의 파티원들 가져오기
        List<PartyPeople> peopleList = partyPeopleRepository.findByParty(party);


        //파티장인지 검증
        boolean isLeader = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE) && partyPeople.getUser().equals(user));
        if(!isLeader){throw new CustomException(ErrorCode.FORBIDDEN_PARTY_LEADER);}

        // 파티원 리스트를 돌면서 나뺴고 모두 없애기
        for(PartyPeople partyPeople : peopleList){
            //나는 파티장이므로 그냥 통과
            if(partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)) continue;
            //나머지는 모두 없애
            partyPeopleRepository.deleteByUserId(partyPeople.getUser().getId());

            //해당 초대의 상태를 추방으로 변경
            Invitation invitation = invitationRepository.findByReceiveUserAndParty(partyPeople.getUser(),party);
            invitation.changeStatus(InvitationStatus.EXILE);
            invitationRepository.save(invitation);
        }
    }
}
