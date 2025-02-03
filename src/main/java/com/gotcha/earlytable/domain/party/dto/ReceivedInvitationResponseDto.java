package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReceivedInvitationResponseDto {

    private final Long invitationId;

    private final String storeName;

    private final Long sendUserId;

    private final LocalDateTime reservationTime;

    private final Integer personnelCount;

    private final InvitationStatus status;

    public ReceivedInvitationResponseDto(Long invitationId, String storeName, Long sendUserId, LocalDateTime reservationTime, Integer personnelCount, InvitationStatus status) {
        this.invitationId = invitationId;
        this.storeName = storeName;
        this.sendUserId = sendUserId;
        this.reservationTime = reservationTime;
        this.personnelCount = personnelCount;
        this.status = status;
    }


    public static ReceivedInvitationResponseDto toDto(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getReservation().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getReservation().getReservationDate()
                .atTime(invitation.getParty().getReservation().getReservationTime());
        Integer personnelCount = invitation.getParty().getReservation().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();

        return new ReceivedInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status);
    }

    public static ReceivedInvitationResponseDto toDtoForReservation(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getReservation().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getReservation().getReservationDate()
                .atTime(invitation.getParty().getReservation().getReservationTime());
        Integer personnelCount = invitation.getParty().getReservation().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();

        return new ReceivedInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status);
    }

    public static ReceivedInvitationResponseDto toDtoForWaiting(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getWaiting().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        LocalDateTime reservationTime = invitation.getParty().getWaiting().getCreatedAt();
        Integer personnelCount = invitation.getParty().getWaiting().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();

        return new ReceivedInvitationResponseDto(invitationId, storeName, sendUserId, reservationTime, personnelCount, status);
    }



}
