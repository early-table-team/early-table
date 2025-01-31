package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.friend.entity.FriendRequest;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

@Getter
public class FriendRequestResponseDto {
    private final Long friendRequestId;
    private final Long sendUserId;
    private final String sendUserNickname;
    private final Long receivedUserId;
    private final InvitationStatus invitationStatus;
    private final String profileImageUrl;

    public FriendRequestResponseDto(Long friendRequestId, Long sendUserId, String sendUserNickname, Long receivedUserId, InvitationStatus invitationStatus, String profileImageUrl) {
        this.friendRequestId = friendRequestId;
        this.sendUserId = sendUserId;
        this.sendUserNickname = sendUserNickname;
        this.receivedUserId = receivedUserId;
        this.invitationStatus = invitationStatus;
        this.profileImageUrl = profileImageUrl;
    }

    public static FriendRequestResponseDto toDto(FriendRequest friendRequest) {
        return new FriendRequestResponseDto(
        friendRequest.getFriendRequestId(),
        friendRequest.getSendUser().getId(),
        friendRequest.getSendUser().getNickName(),
        friendRequest.getReceivedUser().getId(),
        friendRequest.getInvitationStatus(),
        friendRequest.getSendUser().getFile().getFileDetailList().stream().findFirst()
                        .map(FileDetail::getFileUrl).orElse(null)
        );
    }
}
