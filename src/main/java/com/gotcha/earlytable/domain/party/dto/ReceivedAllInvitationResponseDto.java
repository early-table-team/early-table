package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReceivedAllInvitationResponseDto {

    private final Long invitationId;

    private final String storeName;

    private final Long sendUserId;

    private final LocalDateTime reservationTime;

    private final Integer personnelCount;

    private final InvitationStatus status;

    private final String source;

    public ReceivedAllInvitationResponseDto(Long invitationId, String storeName, Long sendUserId, LocalDateTime reservationTime, Integer personnelCount, InvitationStatus status, String source) {
        this.invitationId = invitationId;
        this.storeName = storeName;
        this.sendUserId = sendUserId;
        this.reservationTime = reservationTime;
        this.personnelCount = personnelCount;
        this.status = status;
        this.source = source;
    }


    public static ReceivedAllInvitationResponseDto toDto(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getReservation().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getReservation().getReservationDate()
                .atTime(invitation.getParty().getReservation().getReservationTime());
        Integer personnelCount = invitation.getParty().getReservation().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();
        String source = "";

        return new ReceivedAllInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status, source);
    }

    public static ReceivedAllInvitationResponseDto toDtoForReservation(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getReservation().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getReservation().getReservationDate()
                .atTime(invitation.getParty().getReservation().getReservationTime());
        Integer personnelCount = invitation.getParty().getReservation().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();
        String source = "예약";

        return new ReceivedAllInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status, source);
    }

    public static ReceivedAllInvitationResponseDto toDtoForWaiting(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getWaiting().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getWaiting().getCreatedAt();
        Integer personnelCount = invitation.getParty().getWaiting().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();
        String source = "웨이팅";

        return new ReceivedAllInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status, source);
    }

}
