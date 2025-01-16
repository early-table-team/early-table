package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.domain.friend.entity.FriendRequest;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

@Getter
public class FriendRequestResponseDto {
    private final Long friendRequestId;
    private final Long sendUserId;
    private final Long receivedUserId;
    private final InvitationStatus invitationStatus;

    public FriendRequestResponseDto(Long friendRequestId, Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus) {
        this.friendRequestId = friendRequestId;
        this.sendUserId = sendUserId;
        this.receivedUserId = receivedUserId;
        this.invitationStatus = invitationStatus;
    }

    public static FriendRequestResponseDto toDto(FriendRequest friendRequest) {
        return new FriendRequestResponseDto(
        friendRequest.getFriendRequestId(),
        friendRequest.getSendUser().getId(),
        friendRequest.getReceivedUser().getId(),
        friendRequest.getInvitationStatus()
        );
    }
}
