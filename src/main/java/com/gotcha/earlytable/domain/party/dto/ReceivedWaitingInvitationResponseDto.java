package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReceivedWaitingInvitationResponseDto {

    private final Long invitationId;

    private final String storeName;

    private final Long sendUserId;

    private final Integer personnelCount;

    private final InvitationStatus status;

    public ReceivedWaitingInvitationResponseDto(Long invitationId, String storeName, Long sendUserId, Integer personnelCount, InvitationStatus status) {
        this.invitationId = invitationId;
        this.storeName = storeName;
        this.sendUserId = sendUserId;
        this.personnelCount = personnelCount;
        this.status = status;
    }


    public static ReceivedWaitingInvitationResponseDto toDto(Invitation invitation) {
        Long invitationId = invitation.getInvitationId();
        String storeName = invitation.getParty().getWaiting().getStore().getStoreName();
        Long sendUserId = invitation.getSendUser().getId();
        Integer personnelCount = invitation.getParty().getWaiting().getPersonnelCount();
        InvitationStatus status = invitation.getInvitationStatus();

        return new ReceivedWaitingInvitationResponseDto(invitationId, storeName, sendUserId, personnelCount, status);
    }



}
