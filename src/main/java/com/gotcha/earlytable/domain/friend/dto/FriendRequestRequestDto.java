package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

@Getter
public class FriendRequestRequestDto {
    private final Long sendUserId;
    private final Long receivedUserId;
    private final InvitationStatus invitationStatus;

    public FriendRequestRequestDto(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus) {
        this.sendUserId = sendUserId;
        this.receivedUserId = receivedUserId;
        this.invitationStatus = invitationStatus;
    }
}
