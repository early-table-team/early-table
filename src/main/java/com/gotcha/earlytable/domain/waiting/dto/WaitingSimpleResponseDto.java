package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WaitingSimpleResponseDto {
    Long waitingId;
    Integer waitingNumber;
    Integer personnelCount;
    String phoneNumber;

    public WaitingSimpleResponseDto(Waiting waiting) {
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingNumber();
        this.personnelCount = waiting.getPersonnelCount();
        this.phoneNumber = waiting.getPhone();
    }
}
