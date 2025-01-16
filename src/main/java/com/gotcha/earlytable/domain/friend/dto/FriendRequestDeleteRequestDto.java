package com.gotcha.earlytable.domain.friend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendRequestDeleteRequestDto {

    @NotNull
    private final Long sendUserId;

    @NotNull
    private final Long receivedUserId;

    public FriendRequestDeleteRequestDto(Long sendUserId, Long receivedUserId) {
        this.sendUserId = sendUserId;
        this.receivedUserId = receivedUserId;
    }
}
