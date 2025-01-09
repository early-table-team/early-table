package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WaitingOnlineResponseDto {
    String storeName;
    int personnelCount;
    WaitingType waitingType;
    List<String> partyPeople;
    WaitingStatus waitingStatus;

    public WaitingOnlineResponseDto(Waiting waiting) {
        this.storeName = waiting.getStore().getStoreName();
        this.personnelCount = waiting.getPersonnelCount();
        this.waitingType = waiting.getWaitingType();

        List<String> userNames = new ArrayList<>();

        for (PartyPeople partyPeople : waiting.getParty().getPartyPeople()) {
            userNames.add(String.valueOf(partyPeople.getUser().getNickName()));
        }

        this.partyPeople = userNames;
        this.waitingType = waiting.getWaitingType();
    }
}
