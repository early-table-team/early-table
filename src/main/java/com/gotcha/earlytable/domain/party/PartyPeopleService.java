package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.dto.PartyPeopleResponseDto;
import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyPeopleService {

    private final PartyPeopleRepository partyPeopleRepository;
    private final InvitationRepository invitationRepository;
    private final PartyRepository partyRepository;

    public PartyPeopleService(PartyPeopleRepository partyPeopleRepository, InvitationRepository invitationRepository, PartyRepository partyRepository) {
        this.partyPeopleRepository = partyPeopleRepository;
        this.invitationRepository = invitationRepository;
        this.partyRepository = partyRepository;
    }

    /**
     *  파티 탈퇴 메서드
     * @param invitationId
     * @param user
     */
    @Transactional
    public void leaveInvitation(Long invitationId, User user) {
        //초대 가져오기
        Invitation invitation = invitationRepository.findByInvitationIdOrThrow(invitationId);

        // 파티원의 일원인지 조사를 해야함
        if(invitation.getParty().getPartyPeople().stream().noneMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()))){throw new CustomException(ErrorCode. FORBIDDEN_PARTY_PEOPLE);}

        // 파티장인 경우 못떠나게
        invitation.getParty().getPartyPeople().stream().filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)
               && partyPeople.getUser().getId().equals(user.getId())).findFirst().ifPresent(partyPeople -> {throw new CustomException(ErrorCode.FORBIDDEN_PARTY_LEADER_LEAVE);});

        // 해당 예약의 상태가 수락인지 확인
        if(!invitation.getInvitationStatus().equals(InvitationStatus.ACCEPTED)){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        //초대에서 상태 바꾸기
        invitation.changeStatus(InvitationStatus.LEAVED);
        invitationRepository.delete(invitation);


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
        Party party = partyRepository.findByPartyIdOrThrow(partyId);

        // 일단 초대장 정보도 가져와
        List<Invitation> invitations = invitationRepository.findByPartyOrThrow(party); //로 만들기

        // 해당 유저가 파티원으로 존재하는가?
        Invitation userInvitation = invitations.stream()
                .filter(invitation -> invitation.getReceiveUser().getId().equals(userId))
                .findFirst().orElse(null);

        // 내가 해당 파티의 파티장이 맞는가?
        boolean isPartyLeader = userInvitation.getParty().getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE) && partyPeople.getUser().getId().equals(user.getId()));
        if(!isPartyLeader){throw new CustomException(ErrorCode.FORBIDDEN_PARTY_LEADER);}

        // 파티피플에서 추방 후 초대장 정보를 떠남으로 변경
        partyPeopleRepository.deleteByUserId(userId);
        userInvitation.changeStatus(InvitationStatus.EXILE);
        invitationRepository.delete(userInvitation);

    }

    /**
     *  파티원 전체 추방 메서드
     * @param partyId
     * @param user
     */
    @Transactional
    public void exileAllPartyPeople(Long partyId, User user) {

        // 해당 파티 정보를 가져오기
        Party party = partyRepository.findByPartyIdOrThrow(partyId);

        //해당 파티의 파티원들 가져오기
        List<PartyPeople> peopleList = partyPeopleRepository.findByParty(party);


        //파티장인지 검증
        boolean isLeader = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE) && partyPeople.getUser().getId().equals(user.getId()));
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
            invitationRepository.delete(invitation);
        }
    }

    public List<PartyPeopleResponseDto> getPartyPeople(Long partyId, User user) {
        //파티를 가져오기
        Party party = partyRepository.findByPartyIdOrThrow(partyId);

        // 일단 로그인한 유저가 파티의 일원인 검증을 하는 부분
        boolean isPartyPeople = party.getPartyPeople().stream().anyMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()));
        if(!isPartyPeople){throw new CustomException(ErrorCode.FORBIDDEN_PARTY_PEOPLE);}

        List<PartyPeopleResponseDto> dtos = new ArrayList<>();
        party.getPartyPeople().forEach(partyPeople -> {
            PartyPeopleResponseDto dto = new PartyPeopleResponseDto(partyPeople);
            dtos.add(dto);
        });

        return dtos;

    }
}
