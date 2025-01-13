package com.gotcha.earlytable.domain.friend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class FriendRequestUpdateRequestDto {
    @Enumerated(EnumType.STRING)
    private final InvitationStatus invitationStatus;

    @JsonCreator
    public FriendRequestUpdateRequestDto(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }
}
