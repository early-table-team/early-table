package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReceivedInvitationResponseDto {

    private final Long partyId;

    private final String storeName;

    private final User sendUser;

    private final LocalDateTime reservationTime;

    private final Integer personnelCount;

    private final InvitationStatus status;

    public ReceivedInvitationResponseDto(Long partyId, String storeName, User sendUser, LocalDateTime reservationTime, Integer personnelCount, InvitationStatus status) {
        this.partyId = partyId;
        this.storeName = storeName;
        this.sendUser = sendUser;
        this.reservationTime = reservationTime;
        this.personnelCount = personnelCount;
        this.status = status;
    }

}
