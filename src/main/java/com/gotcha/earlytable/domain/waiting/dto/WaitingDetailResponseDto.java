package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingDetailResponseDto {
    private String storeName;
    private WaitingStatus waitingStatus;
    private WaitingType waitingType;
    private int personnelCount;
    private List<String> partyPeople;

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
