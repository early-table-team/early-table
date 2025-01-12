package com.gotcha.earlytable.domain.party.dto;

import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

@Getter
public class InvitationStatusDto {

    private final InvitationStatus status;

    public InvitationStatusDto(InvitationStatus status) {
        this.status = status;
    }
}
