package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingGetOneResponseDto {
    private final Long waitingId;
    private final Long waitingNumber;
    private final String storeName;
    private final WaitingStatus waitingStatus;
    private final WaitingType waitingType;
    private final int personnelCount;
    private List<String> partyPeople = new ArrayList<>();
    private final String phone;

    public WaitingGetOneResponseDto(Waiting waiting) {
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingNumber();
        this.storeName = waiting.getStore().getStoreName();
        this.waitingStatus = waiting.getWaitingStatus();
        this.waitingType = waiting.getWaitingType();
        this.personnelCount = waiting.getPersonnelCount();
        this.phone = waiting.getPhone();
        if (waiting.getParty() != null) {
            this.partyPeople = waiting.getParty().getPartyPeople().stream()
                    .map(partyPeople -> partyPeople.getUser().getNickName())
                    .collect(Collectors.toList());
        }
    }
}
