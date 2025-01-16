package com.gotcha.earlytable.domain.friend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendRequestUpdateRequestDto {

    @NotNull
    private final InvitationStatus invitationStatus;

    @JsonCreator
    public FriendRequestUpdateRequestDto(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }
}
