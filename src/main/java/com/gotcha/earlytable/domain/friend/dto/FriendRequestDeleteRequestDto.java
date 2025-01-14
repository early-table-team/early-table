package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRequestDeleteRequestDto {
    @NotBlank
    private final Long sendUserId;

    @NotBlank
    private final Long receivedUserId;

    public FriendRequestDeleteRequestDto(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus) {
        this.sendUserId = sendUserId;
        this.receivedUserId = receivedUserId;
    }
}
