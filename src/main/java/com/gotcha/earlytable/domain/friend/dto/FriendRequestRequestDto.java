package com.gotcha.earlytable.domain.friend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRequestRequestDto {
    @NotBlank
    private final Long receivedUserId;

    @JsonCreator
    public FriendRequestRequestDto(Long receivedUserId) {
        this.receivedUserId = receivedUserId;
    }
}
