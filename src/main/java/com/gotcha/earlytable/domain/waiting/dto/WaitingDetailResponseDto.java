package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingDetailResponseDto {
    private final String storeName;
    private final WaitingStatus waitingStatus;
    private final WaitingType waitingType;
    private final int personnelCount;
    private final List<String> partyPeople;

    public WaitingDetailResponseDto (Waiting waiting) {
        this.storeName = waiting.getStore().getStoreName();
        this.waitingStatus = waiting.getWaitingStatus();
        this.waitingType = waiting.getWaitingType();
        this.personnelCount = waiting.getPersonnelCount();
        this.partyPeople = waiting.getParty().getPartyPeople().stream()
                .map(partyPeople -> partyPeople.getUser().getNickName())
                .collect(Collectors.toList());
    }
}
